package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/Ouvrage")
public class Ouvrage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	  private static final String DB_URL = "jdbc:mysql://localhost:3306/mc_projet";
	    private static final String DB_USER = "root";
	    private static final String DB_PASSWORD = "";

	    @Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        String action = request.getParameter("action");

	        if ("add".equals(action)) {
	            addOuvrage(request, response);
	        } else if ("update".equals(action)) {
	            updateOuvrage(request, response);
	        } else if ("delete".equals(action)) {
	            deleteOuvrage(request, response);
	        }
	    }



	    private void addOuvrage(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        try {
	            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	            // Récupérer les données du formulaire
	            String isbn = request.getParameter("ouvrageName");
	            String titre = request.getParameter("ouvrageTitre");
	            String auteur = request.getParameter("ouvrageAuteur");

	            String sql = "INSERT INTO ouvrage (isbn, titre, auteur) VALUES (?, ?, ?)";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                preparedStatement.setString(1, isbn);
	                preparedStatement.setString(2, titre);
	                preparedStatement.setString(3, auteur);

	                // Exécuter la requête SQL
	                int rowsAffected = preparedStatement.executeUpdate();

	                // Vérifier le nombre de lignes affectées pour déterminer le succès
	                if (rowsAffected > 0) {
	                    // Insertion des données réussie
	                    response.sendRedirect("bib.jsp?act=success&message=Insertion réussie"); // Rediriger vers une page de succès
	                } else {
	                    // Échec de l'insertion des données
	                    response.sendRedirect("bib.jsp?act=error&message=Erreur d'insertion"); // Rediriger vers une page d'erreur
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Gérer les exceptions de manière appropriée
	            response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));// Rediriger vers une page d'erreur
	        }
	    }

	    private void updateOuvrage(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        try {
	            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	            // Récupérer les données du formulaire
	            String isbn = request.getParameter("ouvrageName");
	            String titre = request.getParameter("ouvrageTitre");
	            String auteur = request.getParameter("ouvrageAuteur");
	            // Remplacer avec le véritable nom du paramètre mis à jour
	            String updatedValue = request.getParameter("ouvrageNamenew");

	            String sql = "UPDATE ouvrage SET isbn = ?, titre = ?, auteur = ? WHERE isbn = ?";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                preparedStatement.setString(1, updatedValue);
	                preparedStatement.setString(2, titre);
	                preparedStatement.setString(3, auteur);
	                preparedStatement.setString(4, isbn);

	                // Exécuter la requête SQL
	                int rowsAffected = preparedStatement.executeUpdate();

	                // Vérifier le nombre de lignes affectées pour déterminer le succès
	                if (rowsAffected > 0) {
	                    // Mise à jour des données réussie
	                    response.sendRedirect("bib.jsp?act=success&message=Mise a jour reussie"); // Rediriger vers une page de succès
	                } else {
	                    // Échec de la mise à jour des données
	                    response.sendRedirect("bib.jsp?act=error&message=Erreur de mise à jour"); // Rediriger vers une page d'erreur
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Gérer les exceptions de manière appropriée
	            response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8")); // Rediriger vers une page d'erreur
	        }
	    }

	    private void deleteOuvrage(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        try {
	            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	            // Récupérer les données du formulaire
	            String isbn = request.getParameter("ouvrageName");

	            String sql = "DELETE FROM ouvrage WHERE isbn = ?";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                preparedStatement.setString(1, isbn);

	                // Exécuter la requête SQL
	                int rowsAffected = preparedStatement.executeUpdate();

	                // Vérifier le nombre de lignes affectées pour déterminer le succès
	                if (rowsAffected > 0) {
	                    // Suppression des données réussie
	                    response.sendRedirect("bib.jsp?act=success&message=Suppression reussie"); // Rediriger vers une page de succès
	                } else {
	                    // Échec de la suppression des données
	                    response.sendRedirect("bib.jsp?act=error&message=Erreur de suppression"); // Rediriger vers une page d'erreur
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Gérer les exceptions de manière appropriée
	            response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8")); // Rediriger vers une page d'erreur
	        }
	    }


}
