package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@WebServlet("/Emprunter")
public class Emprunter extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userIdObject = (Integer) session.getAttribute("userId");

        if (userIdObject != null) {
            int id = userIdObject.intValue();
            String selectedNumEx = request.getParameter("num_ex");
            String selectedISBN = request.getParameter("ISBN");

            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                String url = "jdbc:mysql://localhost:3306/mc_projet";
                String user = "root";
                String pwd = "";

                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, pwd);

                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateStr = dateFormat.format(currentDate);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.WEEK_OF_YEAR, 3);
                Date dateAfterAddingThreeWeeks = calendar.getTime();
                String dateAfterAddingThreeWeeksStr = dateFormat.format(dateAfterAddingThreeWeeks);

                String selectSql = "SELECT nombre_emprunts,matricule,date_penalisation FROM utilisateurs WHERE id = ?";
                pstmt = conn.prepareStatement(selectSql);
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int nombre_emprunts = rs.getInt("nombre_emprunts");
                    String matricule=rs.getString("matricule");
                    Date datePenalisation = rs.getDate("date_penalisation");

                    if (datePenalisation == null || currentDate.after(datePenalisation)) {
                        if (nombre_emprunts < 3) {
                            String insertSql = "INSERT INTO emprunts (id, num_ex, date_emprunt, date_restitution) VALUES (?, ?, ?, ?)";
                            pstmt = conn.prepareStatement(insertSql);
                            pstmt.setInt(1, id);
                            pstmt.setString(2, selectedNumEx);
                            pstmt.setString(3, currentDateStr);
                            pstmt.setString(4, dateAfterAddingThreeWeeksStr);

                            int rowsInserted = pstmt.executeUpdate();

                            if (rowsInserted > 0) {
                            	
                            	 String receiptContent = generateReceiptContent(id,matricule, selectedNumEx, selectedISBN);

                                // Mise à jour du nombre d'emprunts
                                String updateSql1 = "UPDATE utilisateurs SET nombre_emprunts = nombre_emprunts + 1 WHERE id = ?";
                                pstmt = conn.prepareStatement(updateSql1);
                                pstmt.setInt(1, id);
                                pstmt.executeUpdate();

                                // Mise à jour de l'état de l'exemplaire
                                String updateSql2 = "UPDATE exemplaire SET etat = 'indisponible' WHERE num_ex = ?";
                                pstmt = conn.prepareStatement(updateSql2);
                                pstmt.setString(1, selectedNumEx);
                                pstmt.executeUpdate();

                                
                                
                                request.setAttribute("receiptContent", receiptContent);

                                // Redirect to the confirmation page
                                request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
                                return; // Ensure that no further processing occurs after the forward

                                
                                // Redirect to a confirmation page
                               /* response.sendRedirect(request.getContextPath() + "/emprunter.jsp");
                                return;*/
                            } else {
                                response.sendRedirect("emprunter.jsp?act=error&message=No Rows Inserted"); 
                                System.out.println("No rows inserted!"); // Debug statement
                            }
                        } else {
                            // Message d'erreur : Limite d'emprunts atteinte
                        	  response.sendRedirect("emprunter.jsp?act=error&message=Limite d'emprunts atteinte!"); 
                            System.out.println("Limite d'emprunts atteinte!"); // Debug statement
                        }
                    } else {
                        // Message d'erreur : Pénalisé
                    	 response.sendRedirect("emprunter.jsp?act=error&message=Pénalisé!"); 
                        System.out.println("Pénalisé!"); // Debug statement
                    }
                } else {
                    // Message d'erreur : Utilisateur non trouvé
                	 response.sendRedirect("emprunter.jsp?act=error&message=Utilisateur non trouvé!"); 
                    System.out.println("Utilisateur non trouvé!"); // Debug statement
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace(); // Log the exception
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace(); // Log the exception
                }
            }
        } else {
            // Handle the case where the userId attribute is null
            // Redirect to login or show an error message
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
    
    
    private String generateReceiptContent(int userId,String matricule, String selectedNumEx, String selectedISBN) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder receiptContent = new StringBuilder();
 
        receiptContent.append("<html><body style='font-family: monospace; font-size: 12px;'>")
        .append("<div class='container mt-5'>")
        .append("<div class='row justify-content-center'>")
        .append("<div class='col-md-8'>")
        .append("<div class='card'>")
        .append("<div class='card-header'>")
        .append("<h3 class='text-center'>Reçu Livre Emprunté</h3>")
        .append("</div>")
        .append("<div class='card-body'>")
        .append("<div class='text-center'><p>ID d'emprunteur: ").append(userId).append("</p></div>")
        .append("<div class='text-center'><p>Matricule d'emprunteur: ").append(matricule).append("</p></div>")
        .append("<div class='text-center'><p>ISBN: ").append(selectedISBN).append("</p></div>")
        .append("<div class='text-center'><p>Num_ex: ").append(selectedNumEx).append("</p></div>")
        .append("<div class='text-center'><p>SVP retourner le livre dans <span class='text-danger'> 3 semaines</span> au maximum.</p></div>")
        .append("</div>")
        .append("</div>")
        .append("</div>")
        .append("</div>")
        .append("</div>")
        .append("</body></html>");


        return receiptContent.toString();
    }

            
            
}
