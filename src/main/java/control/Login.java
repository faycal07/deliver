package control;



import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String SESSION_ATTR_LOGIN_G = "loging";
    private static final String SESSION_ATTR_LOGIN_B = "loginb";
    private static final String SESSION_ATTR_LOGIN_ELSE = "loginelse";

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        char[] passwordChars = request.getParameter("password").toCharArray();
        String passwordd = new String(passwordChars);
        String url = "jdbc:mysql://localhost:3306/mc_projet";
        String user = "root";
        String pwd = "";

        if (username == null || username.isEmpty()) {
            showError(response, "Le champ 'Nom d'utilisateur' est obligatoire. Veuillez saisir un nom d'utilisateur.");
            return;
        } else if (passwordd == null || passwordd.isEmpty()) {
            showError(response, "Le champ 'Mot de passe' est obligatoire. Veuillez saisir un mot de passe.");
            return;
        } else {
            try {
                // Perform password encryption if needed
            	String passworddd=encryptPassword(passwordd);
                String password = passworddd;

                // Database connection setup (replace with your database connection code)
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try (Connection con = DriverManager.getConnection(url, user, pwd)) {
                    String query = "SELECT * FROM utilisateurs WHERE username = ? AND password = ?";
                    try (PreparedStatement stmt = con.prepareStatement(query)) {
                        stmt.setString(1, username);
                        stmt.setString(2, password);

                        ResultSet res = stmt.executeQuery();
                        if (res.next()) {
                            String role = res.getString("role");
                            String nom = res.getString("nom");
                            String prenom = res.getString("prenom");
                            redirectToRolePage(response, session, role, res.getInt("id"),nom,prenom);
                        } else {
                        	response.sendRedirect("login.jsp?act=error");

                        }
                    }
                }
            } catch (SQLException ex) {
                // Log the exception using a logging framework like SLF4J
                ex.printStackTrace();
                showError(response, "Une erreur s'est produite lors de l'authentification.");
            }
        }
    }


    public  String encryptPassword(String password) {
try {
 MessageDigest digest = MessageDigest.getInstance("SHA-256");
 byte[] hash = digest.digest(password.getBytes());
 StringBuffer hexString = new StringBuffer();

 for (byte element : hash) {
     String hex = Integer.toHexString(0xff & element);
     if (hex.length() == 1) hexString.append('0');
     hexString.append(hex);
 }

 return hexString.toString();

} catch (NoSuchAlgorithmException e) {
 e.printStackTrace();
}
return null;
}

    private void showError(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<p>" + errorMessage + "</p>");
        out.println("</body></html>");
    }

    private void redirectToRolePage(HttpServletResponse response, HttpSession session, String role, int userId,String nom, String prenom)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");

        if ("Gestionnaire".equals(role)) {
            session.setAttribute(SESSION_ATTR_LOGIN_G, "loging");

            session.setAttribute("nom", nom);

            session.setAttribute("prenom", prenom);
            session.setAttribute("role", role);
            session.setAttribute("userId",userId);
            response.sendRedirect("gestio.jsp?act=success&message=you are logged in as gestionnaire");
        } else if ("Bibliothequaire".equals(role)) {
            session.setAttribute(SESSION_ATTR_LOGIN_B, "loginb");
 session.setAttribute("nom", nom);

            session.setAttribute("prenom", prenom);
            session.setAttribute("role", role);
            session.setAttribute("userId",userId);
            response.sendRedirect("bib.jsp?act=success&message=you are logged in as bibliothequaire");
        } else if ("Enseignant".equals(role) || "Etudiant".equals(role) || "EtudiantExterne".equals(role)) {
            session.setAttribute(SESSION_ATTR_LOGIN_ELSE, "loginelse");
 session.setAttribute("nom", nom);

            session.setAttribute("prenom", prenom);
            session.setAttribute("role", role);
            session.setAttribute("userId",userId);
            response.sendRedirect("emprunter.jsp?act=success&message=you are logged in as Adherent");
        } else {
        	   response.sendRedirect("login.jsp?act=error&message=Role non reconnu");
        }

        out.println("</body></html>");
    }
}

