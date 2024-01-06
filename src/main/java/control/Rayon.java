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
 * Servlet implementation class Rayon
 */
@WebServlet("/Rayon")
public class Rayon extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	  private static final String DB_URL = "jdbc:mysql://localhost:3306/mc_projet";
	    private static final String DB_USER = "root";
	    private static final String DB_PASSWORD = "";

	    @Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        String action = request.getParameter("action2");

	        if ("add".equals(action)) {
	            addRayon(request, response);
	        } else if ("update".equals(action)) {
	            updateRayon(request, response);
	        } else if ("delete".equals(action)) {
	            deleteRayon(request, response);
	        }
	    }




	    private void addRayon(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	try {
	    	  Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	            // Retrieve data from the form
	            String codeRayon = request.getParameter("CodeRayon");
	    	  String sql = "INSERT INTO rayon (code_rayon) VALUES (?)";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                preparedStatement.setString(1, codeRayon);

	                // Execute the SQL statement
	                int rowsAffected = preparedStatement.executeUpdate();

	                // Check the number of rows affected to determine success
	                if (rowsAffected > 0) {
	                    // Data insertion successful
	                	response.sendRedirect("bib.jsp?act=success&message=Inserer avec succes");// Redirect to a success page
	                } else {
	                    // Data insertion failed
	                	response.sendRedirect("bib.jsp?act=error&message=error d insersion"); // Redirect to an error page
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Handle exceptions appropriately
	            response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));// Redirect to an error page
	        }}
	           	    
	    
	    private void updateRayon(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        try {
	            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	            // Retrieve data from the form
	            String codeRayon = request.getParameter("CodeRayon");
	            String updatedValue = request.getParameter("UpdatedValue"); // Replace with the actual parameter name

	            String sql = "UPDATE rayon SET code_rayon = ? WHERE code_rayon = ?";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                preparedStatement.setString(1, updatedValue);
	                preparedStatement.setString(2, codeRayon);

	                // Execute the SQL statement
	                int rowsAffected = preparedStatement.executeUpdate();

	                // Check the number of rows affected to determine success
	                if (rowsAffected > 0) {
	                    // Data update successful
	                    response.sendRedirect("bib.jsp?act=success&message=Mise a jour avec succes"); // Redirect to a success page
	                } else {
	                    // Data update failed
	                    response.sendRedirect("bib.jsp?act=error&message=Erreur de mise a jour"); // Redirect to an error page
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Handle exceptions appropriately
	            response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8")); // Redirect to an error page
	        }
	    }

	    private void deleteRayon(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        try {
	            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	            // Retrieve data from the form
	            String codeRayon = request.getParameter("CodeRayon");

	            String sql = "DELETE FROM rayon WHERE code_rayon = ?";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                preparedStatement.setString(1, codeRayon);

	                // Execute the SQL statement
	                int rowsAffected = preparedStatement.executeUpdate();

	                // Check the number of rows affected to determine success
	                if (rowsAffected > 0) {
	                    // Data deletion successful
	                    response.sendRedirect("bib.jsp?act=success&message=Suppression avec succes"); // Redirect to a success page
	                } else {
	                    // Data deletion failed
	                    response.sendRedirect("bib.jsp?act=error&message=Erreur de suppression"); // Redirect to an error page
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Handle exceptions appropriately
	            response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));// Redirect to an error page
	        }
	    }



}
