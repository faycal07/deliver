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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class Exemplaire
 */
@WebServlet("/Exemplaire")
public class Exemplaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/mc_projet";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String action = request.getParameter("action1");

	    if ("add".equals(action)) {
	        addExemplaire(request, response);
	    } else if ("update".equals(action)) {
	        updateExemplaire(request, response);
	    } else if ("delete".equals(action)) {
	        deleteExemplaire(request, response);
	    }
	}

	private void addExemplaire(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	        // Récupérer les données du formulaire
	        String numEx = request.getParameter("NumEx");
	        String etat = request.getParameter("etat");
	        String ISBN = request.getParameter("ISBN");
	        String codeRayon = request.getParameter("CodeRayon");

	        // Vérifier si l'ISBN existe dans la table "ouvrage"
	        if (!isbnExists(ISBN, connection)) {
	            // ISBN n'existe pas, traitement d'erreur
	            response.sendRedirect("bib.jsp?act=error&message=L'ISBN n'existe pas dans la table 'ouvrage'");
	            return;
	        }else if (!codeRayonExists(codeRayon, connection)) {
	            // codeRayon n'existe pas, traitement d'erreur
	            response.sendRedirect("bib.jsp?act=error&message=Le codeRayon n'existe pas dans la table 'rayon'");
	            return;
	        }else {
	        	
	        	 String sql = "INSERT INTO exemplaire (num_ex, etat, ISBN, code_rayon) VALUES (?, ?, ?, ?)";
	 	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	 	            preparedStatement.setString(1, numEx);
	 	            preparedStatement.setString(2, etat);
	 	            preparedStatement.setString(3, ISBN);
	 	            preparedStatement.setString(4, codeRayon);

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
	        	
	        }

	       

	       
	    } catch (SQLException e) {
	        e.printStackTrace(); // Gérer les exceptions de manière appropriée
	        response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));// Rediriger vers une page d'erreur
	    }
	}

	private void updateExemplaire(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	        // Récupérer les données du formulaire
	        String numEx = request.getParameter("NumEx");
	        String etat = request.getParameter("etat");
	        String ISBN = request.getParameter("ISBN");
	        String codeRayon = request.getParameter("CodeRayon");
	        // Remplacer avec le véritable nom du paramètre mis à jour
	        String updatedValue = request.getParameter("NumExNew");

	        // Vérifier si l'ISBN existe dans la table "ouvrage"
	        if (!isbnExists(ISBN, connection)) {
	            // ISBN n'existe pas, traitement d'erreur
	            response.sendRedirect("bib.jsp?act=error&message=L'ISBN n'existe pas dans la table 'ouvrage'");
	            return;
	        }else if(!codeRayonExists(codeRayon, connection)) {
	            // codeRayon n'existe pas, traitement d'erreur
	            response.sendRedirect("bib.jsp?act=error&message=Le codeRayon n'existe pas dans la table 'rayon'");
	            return;
	        }else {
	        	
	        	System.out.println("heloo");
	        	  String sql = "UPDATE exemplaire SET num_ex = ?, etat = ?, ISBN = ?, code_rayon = ? WHERE num_ex = ?";
	  	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	  	            preparedStatement.setString(1, updatedValue);
	  	            preparedStatement.setString(2, etat);
	  	            preparedStatement.setString(3, ISBN);
	  	            preparedStatement.setString(4, codeRayon);
	  	            preparedStatement.setString(5, numEx);

	  	            // Exécuter la requête SQL
	  	            int rowsAffected = preparedStatement.executeUpdate();

	  	            // Vérifier le nombre de lignes affectées pour déterminer le succès
	  	            if (rowsAffected > 0) {
	  	                // Mise à jour des données réussie
	  	                response.sendRedirect("bib.jsp?act=success&message=Mise à jour réussie"); // Rediriger vers une page de succès
	  	            } else {
	  	                // Échec de la mise à jour des données
	  	                response.sendRedirect("bib.jsp?act=error&message=Erreur de mise à jour"); // Rediriger vers une page d'erreur
	  	            }
	  	        }
	        }

	       
	       

	      
	    } catch (SQLException e) {
	        e.printStackTrace(); // Gérer les exceptions de manière appropriée
	        response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8")); // Rediriger vers une page d'erreur
	    }
	}

	// Fonction pour vérifier si l'ISBN existe dans la table "ouvrage"
	private boolean isbnExists(String isbn, Connection connection) throws SQLException {
	    String query = "SELECT COUNT(*) FROM ouvrage WHERE ISBN = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, isbn);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            resultSet.next();
	            return resultSet.getInt(1) > 0;
	        }
	    }
	}

	// Fonction pour vérifier si le codeRayon existe dans la table "rayon"
	private boolean codeRayonExists(String codeRayon, Connection connection) throws SQLException {
	    String query = "SELECT COUNT(*) FROM rayon WHERE code_rayon = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, codeRayon);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            resultSet.next();
	            return resultSet.getInt(1) > 0;
	        }
	    }
	}
	private void deleteExemplaire(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	        // Récupérer les données du formulaire
	        String numEx = request.getParameter("NumEx");

	        String sql = "DELETE FROM exemplaire WHERE num_ex = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	            preparedStatement.setString(1, numEx);

	            // Exécuter la requête SQL
	            int rowsAffected = preparedStatement.executeUpdate();

	            // Vérifier le nombre de lignes affectées pour déterminer le succès
	            if (rowsAffected > 0) {
	                // Suppression des données réussie
	                response.sendRedirect("bib.jsp?act=success&message=Suppression réussie"); // Rediriger vers une page de succès
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
