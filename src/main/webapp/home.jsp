<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
<link href="bootstrap-5.3.2-dist/css/bootstrap.min.css" type="text/css" rel="stylesheet">
<link href="style.css" type="text/css" rel="stylesheet">
</head>
<body>





  <jsp:include page="nav.jsp" />








  <!-- Featured Section -->
    <div class="container border p-5 bg-secondary-subtle rounded-5" style="margin-top: 75px">
        <div class="row">
            <div class="col-lg-8 mt-5">
                <h1 class="p-5 ">Welcome to the University Library</h1>
                <p class="lead p-5">Explore our vast collection of books, journals, and digital resources.</p>
                
                
              <div class="text-center">
                       <!--  <a class="btn btn-warning rounded-5" href="inscrire.jsp">Inscrire</a> -->
               
                        <a class="btn btn-secondary rounded-5" href="login.jsp">Connecter</a></div>
            
                
               
                   
              
              
                
            </div>
            <div class="col-lg-4 ">
                <img src="dana-ward-18SA3cMtep8-unsplash.jpg" class="img-fluid rounded" alt="Library Image">
            </div>
        </div>
    </div>

    <!-- Sample Content -->
    <div class="container mt-4">
        <h2 class="text-center text-decoration-underline px-5 my-5 ">Recent Arrivals</h2>
        <div class="row">
          
           <%
    String url = "jdbc:mysql://localhost:3306/mc_projet";
    String user = "root";
    String pwd = "";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pwd);

        String query = "SELECT e.num_ex, e.ISBN, o.titre, o.auteur FROM exemplaire e INNER JOIN ouvrage o ON e.ISBN = o.ISBN WHERE e.etat = 'disponible' ORDER BY e.id DESC LIMIT 3";

        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
%>


                
<%
    while (rs.next()) {
%>
           
         
            <div class="col-md-4 mb-4">
                <div class="card">
                    <img src="https://picsum.photos/30/30" class="card-img-top" alt="Book 3">
                    <div class="card-body">
                        <h5 class="card-title text-warning fs-2"><%= rs.getString("titre") %></h5>
                        <p class="card-text">Author: <%= rs.getString("auteur") %></p>
                        <p>ISBN:<%= rs.getString("ISBN") %></p>
                         <p>Numéro Exemplaire:<%= rs.getString("num_ex") %></p>
                        
                        <a href="login.jsp" class="btn btn-primary">EMPRUNTER</a>
                    </div>
                </div>
            </div>
      
   


<%
    }
    rs.close();
    pst.close();
    con.close();
} catch (Exception e) {
    e.printStackTrace();
}
%>
      
  </div>
 </div>




  <jsp:include page="footer.jsp" />


<script  src="bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>