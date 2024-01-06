<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    <%@ page import="jakarta.servlet.ServletException" %>
<%@ page import="jakarta.servlet.annotation.WebServlet" %>
<%@ page import="jakarta.servlet.http.HttpServlet" %>
<%@ page import="jakarta.servlet.http.HttpServletRequest" %>
<%@ page import="jakarta.servlet.http.HttpServletResponse" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.io.UnsupportedEncodingException" %>

<%
session = request.getSession();


//Check if the user is already logged in

 String role = (String) session.getAttribute("role");




     
   

 if ("Gestionnaire".equals(role)) {
     response.sendRedirect("gestio.jsp");
 } else if ("Enseignant".equals(role) || "Etudiant".equals(role) || "EtudiantExterne".equals(role)) {
     response.sendRedirect("emprunter.jsp");
 } else if ("Bibliothequaire".equals(role)) {
     response.sendRedirect("bib.jsp");
 }

%>
 
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link href="bootstrap-5.3.2-dist/css/bootstrap.min.css" rel="stylesheet">
<link href="style.css" rel="stylesheet">
</head>
<body>


        

<div class="container-fluid px-0">

 <jsp:include page="nav.jsp" />



</div>




<div class="bg-image" style="background-image: url('dana-ward-18SA3cMtep8-unsplash.jpg'); height: 100vh; background-size: cover;">

 <div class="container col-md-6">
   
 <div class="container text-white py-5 ">
        <h2 class="mb-1 text-center py-5">Formulaire de Login</h2>
        
           <% if ("error".equals(request.getParameter("act"))) { %>
                <!-- Display error message if act parameter is set to error -->
                <div class="alert alert-danger" role="alert">
                    Le champ 'Mot de passe' ou 'Username' est incorrect.
                </div>
            <% } %>
            
            
        <form class="align-middle" method="post" action="Login">
         
        
         
         
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input type="text" class="form-control" id="username" name="username" required>
            </div>
            <!-- Password -->
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>


            <!-- Submit Button -->
            <div class="text-center mb-5 ">
               <button type="submit" class="btn btn-warning  rounded-5">Login</button>
              <button type="reset" class="btn btn-danger rounded-5">Reset</button>
            </div>
         
           
        </form>
        
        </div>
        </div>
        
        
    </div>
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
      <jsp:include page="footer.jsp" />

    <!-- Custom JavaScript to show/hide role-specific information -->
    <script>
        document.getElementById('role').addEventListener('change', function () {
            // Hide all role-specific information divs
            document.getElementById('etudiantInfo').style.display = 'none';
            document.getElementById('etudiantExterneInfo').style.display = 'none';
            document.getElementById('enseignantInfo').style.display = 'none';

            // Show the selected role-specific information div
            var role = this.value;
            if (role === 'etudiant') {
                document.getElementById('etudiantInfo').style.display = 'block';
            } else if (role === 'etudiant_externe') {
                document.getElementById('etudiantExterneInfo').style.display = 'block';
            } else if (role === 'enseignant') {
                document.getElementById('enseignantInfo').style.display = 'block';
            }
        });
     
      

   
        
        <% 

        String act = request.getParameter("act");
       
        if ("success".equals(act)) { %>
        alert("you are logged in");
    <% } else if ("error".equals(act)) {  %>
           
             
       alert("Le champ 'Mot de passe' ou 'Username' est incorrecte");
    
  <%   }%>
    </script>







<script  src="bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>