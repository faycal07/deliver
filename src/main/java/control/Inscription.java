package control;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Inscription")
public class Inscription extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/mc_projet";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addUser(request, response);
        } else if ("update".equals(action)) {
            updateUser(request, response);
        } else if ("delete".equals(action)) {
            deleteUser(request, response);
        }
    }




    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String nom = request.getParameter("Nom");
            String prenom = request.getParameter("Prenom");
            String dateNaiss = request.getParameter("DateNaiss");
            String universite = request.getParameter("Universite");
            String username = request.getParameter("Username");
            char[] passwordChars = request.getParameter("Password").toCharArray();
            String passwordd = new String(passwordChars);
            String password=encryptPassword(passwordd);

            String role = request.getParameter("Role");
            String matricule = request.getParameter("Matricule");
            String datePenalisation = request.getParameter("DatePenalisation");
            String grade = request.getParameter("Grade");
            String anneeEtude = request.getParameter("AnneeEtude");
            String payert = request.getParameter("Payer");

            int payer = "on".equals(payert) ? 1 : 0;
            int totalInscrits = getTotalInscrits(connection);
            int etudiantsExternesInscrits = getEtudiantsExternesInscrits(connection);
            // Prepare the SQL statement
            String sql = "";
            if ("Enseignant".equals(role)) {
                sql = "INSERT INTO utilisateurs (nom, prenom, date_naiss, username, password, role, matricule, date_penalisation, grade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            } else if ("Etudiant".equals(role)) {
                sql = "INSERT INTO utilisateurs (nom, prenom, date_naiss, username, password, role, matricule, date_penalisation, annee_etude, payer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            } else if ("EtudiantExterne".equals(role)) {
            	 if (!isInscriptionAutorisee(etudiantsExternesInscrits, totalInscrits)) {
                     // Refuser l'inscription et rediriger avec un message d'erreur
                     response.sendRedirect("gestio.jsp?act=error&message=Inscription non autorisee");
                     return;
                 }else {
                	 sql = "INSERT INTO utilisateurs (nom, prenom, date_naiss, universite, username, password, role, matricule, date_penalisation, annee_etude, payer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
                 }
            	
            	
               
            } else if ("Bibliothequaire".equals(role) || "Gestionnaire".equals(role)) {
                sql = "INSERT INTO utilisateurs (nom, prenom, date_naiss, username, password, role, matricule) VALUES (?, ?, ?, ?, ?, ?, ?)";
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int index = 1; // Parameter index

                // Set common parameters
                statement.setString(index++, nom);
                statement.setString(index++, prenom);
                statement.setString(index++, dateNaiss);

                // Set role-specific parameters
                if ("EtudiantExterne".equals(role)) {
                    statement.setString(index++, universite);
                    statement.setString(index++, username);
                } else {
                    statement.setString(index++, username);
                }
                statement.setString(index++, password);
                statement.setString(index++, role);
                statement.setString(index++, matricule);

                if ("Enseignant".equals(role) || "Etudiant".equals(role) || "EtudiantExterne".equals(role)) {
                    statement.setString(index++, datePenalisation);
                }
                if ("Enseignant".equals(role)) {
                    statement.setString(index++, grade);
                }
                if ("Etudiant".equals(role) || "EtudiantExterne".equals(role)) {
                    statement.setString(index++, anneeEtude);
                    statement.setInt(index++, payer);
                }

                // Execute the statement
                statement.executeUpdate();
            }

            connection.close();

            response.sendRedirect("gestio.jsp?act=success");
           // Redirect to a success page
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("gestio.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
    
    
    
    

    ////////////////////////////////////////////
    
    
    
    
    
    
    
    
    
    
    
   

            // Vérifier si l'inscription d'un nouvel étudiant externe est autorisée
           
    
    
    
    
    
    
    
    
    private int getTotalInscrits(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM utilisateurs");
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;
        }
    }

    private int getEtudiantsExternesInscrits(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS externes FROM utilisateurs WHERE role = 'EtudiantExterne'");
            if (resultSet.next()) {
                return resultSet.getInt("externes");
            }
            return 0;
        }
    }

    private boolean isInscriptionAutorisee(int etudiantsExternesInscrits, int totalInscrits) {
        double seuilPourcentage = 0.10; // Seuil de 10%
        double pourcentageEtudiantsExternes = (double) etudiantsExternesInscrits / totalInscrits;
        return pourcentageEtudiantsExternes <= seuilPourcentage;
    }
    
    
    
    
    
    
    /////////////////////////////
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

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String userId = request.getParameter("ID");
            String nom = request.getParameter("Nom");
            String prenom = request.getParameter("Prenom");
            String dateNaiss = request.getParameter("DateNaiss");
            String universite = request.getParameter("Universite");
            String username = request.getParameter("Username");
            char[] passwordChars = request.getParameter("Password").toCharArray();
            String passwordd = new String(passwordChars);
            String password=encryptPassword(passwordd);

            String role = request.getParameter("Role");
            String matricule = request.getParameter("Matricule");
            String datePenalisation = request.getParameter("DatePenalisation");
            String grade = request.getParameter("Grade");
            String anneeEtude = request.getParameter("AnneeEtude");
            String payert = request.getParameter("Payer");

            int payer = "on".equals(payert) ? 1 : 0;
    
            // Prepare the SQL statement
            String sql = "";
            if ("Enseignant".equals(role)) {
                sql = "UPDATE utilisateurs SET nom=?, prenom=?, date_naiss=?, username=?, password=?,role=?, matricule=?,  grade=? WHERE id=?";
            } else if ("Etudiant".equals(role)) {
                sql = "UPDATE utilisateurs SET nom=?, prenom=?, date_naiss=?, username=?, password=?,role=?, matricule=?,  annee_etude=?, payer=? WHERE id=?";
            } else if ("EtudiantExterne".equals(role)) {
                sql = "UPDATE utilisateurs SET nom=?, prenom=?, date_naiss=?, universite=?, username=?, password=?,role=?, matricule=?, annee_etude=?, payer=? WHERE id=?";
            } else if ("Bibliothequaire".equals(role) || "Gestionnaire".equals(role)) {
                sql = "UPDATE utilisateurs SET nom=?, prenom=?, date_naiss=?, username=?, password=?,role=?, matricule=? WHERE id=?";
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int index = 1; // Parameter index

                // Set common parameters
                statement.setString(index++, nom);
                statement.setString(index++, prenom);
                statement.setString(index++, dateNaiss);

                // Set role-specific parameters
                if ("EtudiantExterne".equals(role)) {
                    statement.setString(index++, universite);
                    statement.setString(index++, username);
                } else {
                    statement.setString(index++, username);
                }
                statement.setString(index++, password);
                statement.setString(index++, role);
              
                statement.setString(index++, matricule);

             
                if ("Enseignant".equals(role)) {
                    statement.setString(index++, grade);
                }
                if ("Etudiant".equals(role) || "EtudiantExterne".equals(role)) {
                    statement.setString(index++, anneeEtude);
                    statement.setInt(index++, payer);
                }
                statement.setString(index++, userId);

                // Execute the statement
                statement.executeUpdate();
            }

            connection.close();

            response.sendRedirect("gestio.jsp?act=success&message=update reussit");
           // Redirect to a success page
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("gestio.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
    


    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Extract parameters from the request
            // Similar to the addUser method, but retrieve user ID or unique identifier for deletion

            String userId = request.getParameter("ID"); // Assuming there is a parameter for user ID

            // Check if the user ID is provided
            if (userId != null && !userId.isEmpty()) {
                // Proceed with the deletion logic
                String sql = "DELETE FROM utilisateurs WHERE id=?";
                
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    // Set the parameter to the user ID
                    statement.setString(1, userId);

                    // Execute the delete statement
                    statement.executeUpdate();
                }
            } else {
                // Handle the case where the user ID is not provided
                response.sendRedirect("gestio.jsp?act=error&message=User ID not provided for deletion");
            }

            connection.close();

            response.sendRedirect("gestio.jsp?act=success&message=delete reussit");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("gestio.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
    
    
   




}




