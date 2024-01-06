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

/**
 * Servlet implementation class Prolonger
 */
@WebServlet("/Prolonger")
public class Prolonger extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String DB_URL = "jdbc:mysql://localhost:3306/mc_projet";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {


	   
	    try {
	        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	        // Récupérer les données du formulaire
	        String id = request.getParameter("id");
	        String num_ex = request.getParameter("num_ex");
	     
	 

	        	
	        	System.out.println("heloo");
	        	String query = "UPDATE emprunts SET date_restitution = DATE_ADD(date_restitution, INTERVAL 2 WEEK) WHERE id = ? AND num_ex = ?";
	  	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	  	            preparedStatement.setString(1, id);
	  	          preparedStatement.setString(2, num_ex);
	  	         
	  	    

	  	            // Exécuter la requête SQL
	  	            int rowsAffected = preparedStatement.executeUpdate();

	  	            // Vérifier le nombre de lignes affectées pour déterminer le succès
	  	            if (rowsAffected > 0) {
	  	                // Mise à jour des données réussie
	  	                response.sendRedirect("bib.jsp?act=success&message=Prolonger avec succes#gempruntPage"); // Rediriger vers une page de succès
	  	            } else {
	  	                // Échec de la mise à jour des données
	  	                response.sendRedirect("bib.jsp?act=error&message=Erreur de prolongation#gempruntPage"); // Rediriger vers une page d'erreur
	  	            }
	  	        }
	        

	       
	       

	      
	    } catch (SQLException e) {
	        e.printStackTrace(); // Gérer les exceptions de manière appropriée
	        response.sendRedirect("gestio.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8")); // Rediriger vers une page d'erreur
	    }
	}


}
