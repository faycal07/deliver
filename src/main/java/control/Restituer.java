package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet implementation class Restituer
 */



@WebServlet("/Restituer")
public class Restituer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       


   


	   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	       String num_ex = request.getParameter("NumEx");
	       String ISBN = request.getParameter("ISBN");
	       String matricule = request.getParameter("matricule");
	       SimpleDateFormat sp = new SimpleDateFormat("yyyy/MM/dd");

	       Date currentDate = new Date();
	       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	       String date_restitution = dateFormat.format(currentDate);

	     

	       try {
	           Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mc_projet","root", "");

	           String query = "UPDATE exemplaire SET etat='disponible' WHERE ISBN=? AND num_ex=?";
	           PreparedStatement stmt1 = conn.prepareStatement(query);
	           stmt1.setString(1, ISBN);
	           stmt1.setString(2, num_ex);
	           System.out.println("Executing Query: " + stmt1.toString());
	           stmt1.executeUpdate();

	           String query1 = "UPDATE utilisateurs SET nombre_emprunts=nombre_emprunts-1 WHERE matricule=?";
	           PreparedStatement stmt2 = conn.prepareStatement(query1);
	           stmt2.setString(1, matricule);
	           System.out.println("Executing Query: " + stmt2.toString());
	           stmt2.executeUpdate();

	           String req = "SELECT id from utilisateurs where matricule=?";
	           PreparedStatement pstmt1 = conn.prepareStatement(req);
	           pstmt1.setString(1, matricule);
	           ResultSet rs1 = pstmt1.executeQuery();
	           if (rs1.next()) {
	               String id = rs1.getString("id");

	               String query2 = "SELECT * FROM emprunts WHERE num_ex=? AND id=?";
	               PreparedStatement pstmt2 = conn.prepareStatement(query2);
	               pstmt2.setString(1, num_ex);
	               pstmt2.setString(2, id);
	               ResultSet rs2 = pstmt2.executeQuery();
	               if (rs2.next()) {
	                  String date_restitution_db = rs2.getString("date_restitution");
	                  if (date_restitution.compareTo(date_restitution_db) > 0) {
	                      String query3 = "UPDATE utilisateurs SET a_penaliser=1 WHERE matricule=?";
	                      PreparedStatement pstmt3 = conn.prepareStatement(query3);
	                      pstmt3.setString(1, matricule);
	                      pstmt3.executeUpdate();
	                      System.out.println("Executing Query: " + pstmt3.toString());
	                      pstmt3.close();
	                  }
	               }
	               rs2.close();
	               pstmt2.close();

	               String query22 = "DELETE FROM emprunts WHERE num_ex=? AND id=?";
	               PreparedStatement pstmt22 = conn.prepareStatement(query22);
	               pstmt22.setString(1, num_ex);
	               pstmt22.setString(2, id);
	               pstmt22.executeUpdate();
	               System.out.println("Executing Query: " + pstmt22.toString());
	               pstmt22.close();
	           }
	           rs1.close();
	           pstmt1.close();

	           conn.close();

	           response.sendRedirect("bib.jsp?act=success&message=Restitue avec succes");
	       } catch (SQLException e) {
	    	   e.printStackTrace();
	           response.sendRedirect("bib.jsp?act=error&message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
	       }
	   }
	}

    	




