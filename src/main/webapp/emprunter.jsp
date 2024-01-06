<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
   <%@ page import="jakarta.servlet.http.HttpSession" %>
 
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>


    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Espace Adhérent</title>

   
   <link href="bib.css" type="text/css" rel="stylesheet">

  
    <link href="bootstrap-5.3.2-dist/css/bootstrap.min.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" >
    

    <link href="style.css" type="text/css" rel="stylesheet">
</head>

<body>
 <jsp:include page="nav.jsp" />
 <%
    String act = request.getParameter("act");
    String message = request.getParameter("message");
%>


 

<%
     session= request.getSession(false);
    if (session == null || session.getAttribute("role") == null) {
        // User is not logged in
%>
        <h1>Access Denied</h1>
        <p>You must log in to access this page.</p>
<%
    } else {
        String role = (String) session.getAttribute("role");
        if ("Enseignant".equals(role) || "Etudiant".equals(role) || "EtudiantExterne".equals(role)) {
        	
        	String nom = (String) session.getAttribute("nom");
    	    String prenom = (String) session.getAttribute("prenom");
            // User is logged in and has the GESTIONNAIRE role
%>
 
 <div class="container-fluid m-0 p-0 mt-5 pt-2">
 
 <% if ("error".equals(act)) { %>
        <!-- Display error message if act parameter is set to error -->
        <div class="alert alert-danger" id="errorMessage">
            <%= message %>
        </div>
    <% } else if ("success".equals(act)) { %>
        <!-- Display success message if act parameter is set to success -->
        <div class="alert alert-success" id="successMessage">
            <%= message %>
        </div>
    <% } act=null;%>
 
 
    <header class="headerq">
        <a href="#" class="logoq">Espace Adhérent</a>
        <nav class="navbarq">
          <div class="user-infoq" style="text-align:center">
    <span class="nameq"><%= nom %></span>
    <span class="nameq"><%= prenom %></span>
    
</div>
<div><i class="fas fa-user"></i></div>
        </nav>
    </header>

   <div class="containerq ">
        <div class="sidebarq ">
            <h2><%= role %></h2>
    <!--      <form action="">
         
          <label for="ISBN">ISBN:</label>
                <input type="text" id="ISBN" name="ISBN" required>
                   
                <label for="ouvrageTitre">Titre:</label>
                <input type="text" id="ouvrageTitre" name="ouvrageTitre" required>
                     <label for="ouvrageAuteur">Auteur:</label>
                <input type="text" id="ouvrageAuteur" name="ouvrageAuteur" required>
         
         <button type="submit" class="btn btn-outline-warning m-3 ms-5">Search</button>
         </form>
           -->
            <a href="Logout">Se Déconnecter</a>
        </div>

       
<div class="container">
    <h1 class="my-4">Liste des Exemplaires Disponibles</h1>

    <!-- Search Form -->
    <form method="post">
        <div class="form-group">
            <label for="searchInput">(oubien les 3 par ordre)Titre,Auteur,ou ISBN :</label>
            <input type="text" class="w-25" id="searchInput" name="searchInput" required>
            <button type="submit" class="btn btn-outline-warning m-3">Rechercher</button>
            <a href="<%= request.getRequestURI() %>" class="btn btn-outline-secondary m-3">Refresh</a>
        </div>
    </form>

    <%
    String url = "jdbc:mysql://localhost:3306/mc_projet";
    String user = "root";
    String pwd = "";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pwd);

        // Handle Search Input
       /* String searchInput = request.getParameter("searchInput");
        String query = "SELECT e.num_ex,  o.titre, o.auteur,e.ISBN FROM exemplaire e INNER JOIN ouvrage o ON e.ISBN = o.ISBN WHERE e.etat = 'disponible'";

        if (searchInput != null && !searchInput.isEmpty()) {
            query += " AND (o.titre LIKE ? OR o.auteur LIKE ? OR o.ISBN LIKE ?)";
        }
*/
String searchInput = request.getParameter("searchInput");
String[] keywords = null;

// Vérifiez si searchInput n'est pas vide et divisez les mots clés
if (searchInput != null && !searchInput.isEmpty()) {
    keywords = searchInput.split("\\s+");
}

String query = "SELECT e.num_ex, o.titre, o.auteur, e.ISBN FROM exemplaire e INNER JOIN ouvrage o ON e.ISBN = o.ISBN WHERE e.etat = 'disponible'";

if (keywords != null && keywords.length > 0) {
    query += " AND (";

    for (int i = 0; i < keywords.length; i++) {
        if (i > 0) {
            query += " AND ";
        }
        query += "(o.titre LIKE ? OR o.auteur LIKE ? OR e.ISBN LIKE ?)";
    }

    query += ")";
}


       /* 

        if (searchInput != null && !searchInput.isEmpty()) {
            pst.setString(1, "%" + searchInput + "%");
            pst.setString(2, "%" + searchInput + "%");
            pst.setString(3, "%" + searchInput + "%");
        }*/
        PreparedStatement pst = con.prepareStatement(query);
        
        if (keywords != null && keywords.length > 0) {
            // Paramètres dynamiques pour la recherche
            for (int i = 0; i < keywords.length; i++) {
                String keyword = "%" + keywords[i] + "%";
                pst.setString(i * 3 + 1, keyword); // Titre
                pst.setString(i * 3 + 2, keyword); // Auteur
                pst.setString(i * 3 + 3, keyword); // ISBN
            }
        }

        ResultSet rs = pst.executeQuery();
    %>

    <!-- Display Search Results -->
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>Numéro</th>
                                <th>ISBN</th>
                                <th>Titre</th>
                                <th>Auteur</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                            if (!rs.next()) {
                            %>
                            <tr>
                                <td colspan="5">
                                    <h3>Ce livre n'est pas disponible!</h3>
                                </td>
                            </tr>
                               <!-- Insert into acquerir_livre table -->
    <%
   /* String insertQuery = "INSERT INTO acquerir_livre (titre, auteur, ISBN) VALUES (?, ?, ?)";
    PreparedStatement insertPst = con.prepareStatement(insertQuery);
    insertPst.setString(1, searchInput);
    insertPst.setString(2, searchInput);
    insertPst.setString(3, searchInput);
    insertPst.executeUpdate();
    insertPst.close();*/
    

 
    String[] words = searchInput.split("\\s+"); // Split the searchInput into words
    String insertQuery = "INSERT INTO acquerir_livre (titre, auteur, ISBN) VALUES (?, ?, ?)";
    PreparedStatement insertPst = con.prepareStatement(insertQuery);

    // Insert words into acquerir_livre based on the order
    for (int i = 0; i < 3; i++) {
        String word = (i < words.length) ? words[i] : ""; // Avoid IndexOutOfBoundsException
        insertPst.setString(i + 1, word);
    }

    insertPst.executeUpdate();
    insertPst.close();
   
    
    %>
                            
                            
                            <%
                     
                            
                            } else {
                                do {
                            %>
                            <tr>
                                <td><%= rs.getString("num_ex") %></td>
                                <td><%= rs.getString("ISBN") %></td>
                                <td><%= rs.getString("titre") %></td>
                                <td><%= rs.getString("auteur") %></td>
                                <td>
                                    <form action="Emprunter" method="post" onsubmit="return showConfirmation()">
                                        <input type="hidden" name="num_ex" value="<%= rs.getString("num_ex") %>">
                                        <input type="hidden" name="ISBN" value="<%= rs.getString("ISBN") %>">
                                        <button type="submit" class="btn btn-primary">EMPRUNTER</button>
                                    </form>
                                </td>
                            </tr>
                            <%
                                } while (rs.next());
                            }
                            rs.close();
                            pst.close();
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <%
        } catch (Exception e) {
            e.printStackTrace();
        }
    %>
</div>



    
</div>
   


       
  
<%
        } else {
            // User is logged in but does not have the GESTIONNAIRE role
%>
            <h1>Access Denied</h1>
            <p>You do not have permission to access this page.</p>
<%
        }
    }
%>
    
    
  <jsp:include page="footer.jsp" />
  
  
       <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script type="text/javascript" src="gestio.js"></script>

    <script>
    $(document).ready(function () {
        setTimeout(function () {
            $("#errorMessage, #successMessage").fadeOut(500);
        }, 3000);
    });

  

        
        
    function showConfirmation() {
        // Customize your confirmation message and appearance
        var confirmationMessage = "Are you sure you want to EMPRINT this book?";
        var confirmationTitle = "Confirmation"; // Custom title (optional)

        // Use a custom-styled dialog or the browser's default confirm
        var userConfirmed = confirm(confirmationMessage);

        // Return true to proceed with the form submission or false to cancel
        return userConfirmed;
    }

        
       
        
        
    </script>

<script  src="bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"></script>

</body>

</html>