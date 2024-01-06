<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Espace Bibliothècaire</title>
    <link rel="stylesheet" type="text/css" href="bib.css?ee">
    <link href="bootstrap-5.3.2-dist/css/bootstrap.min.css" type="text/css" rel="stylesheet">
<link href="style.css" type="text/css" rel="stylesheet">
 <style>
        /* Define a media query to hide the button when printing */
        @media print {
            .print-button {
                display: none !important;
            }
        }
    </style>
</head>

<body>
 
 <%
    String act = request.getParameter("act");
    String message = request.getParameter("message");
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
    <% } else {%>        <span type="hidden" ></span>  <% } act="";%>


 <jsp:include page="nav.jsp" />

 
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
        if ("Bibliothequaire".equals(role)) {
        	
        	
        	  String nom = (String) session.getAttribute("nom");
        	    String prenom = (String) session.getAttribute("prenom");
            // User is logged in and has the GESTIONNAIRE role
%>
 

    

     
    <header class="headerq">
        <a href="#" class="logoq">Espace Bibliothèquaire</a>
        <nav class="navbarq">
            <div class="user-infoq">
               <span class="nameq"><%= nom %></span>
                <span class="nameq"><%= prenom %></span>
                <div class="photo-iconq"></div>
                
                
                
            </div>
        </nav>
        
    </header>

    <div class="containerq">
        <div class="sidebarq">
          <h2><%= role %></h2>
            <a href="#" onclick="showPage('ouvrage')">Gestion des Ouvrages</a>
              <a href="#" onclick="showPage('exemplaire')">Gestion des Exemplaires</a>
                <a href="#" onclick="showPage('rayon')">Gestion des Rayons</a>
            <a href="#" onclick="showPage('aquisition')">Acquisition Des Ouvrages</a>
            <a href="#" onclick="showPage('gemprunt')">Rapports d'Emprunts</a>
            <a href="#" onclick="showPage('remprunt')">Restitution Des Emprunts</a>
            <a href="Logout">Se Déconnecter</a>
        </div>

        <div class="contentq" id="ouvragePage">
            <h1>Gestion d'ouvrage</h1>
          
              <form class="formq" id="ouvrageForm" action="Ouvrage" method="post">
              <input type="hidden" name="action" id="action" value="" />
                <label for="ouvrageName">ISBN:</label>
                <input type="text" id="ouvrageName" name="ouvrageName" required>
                     <label for="ouvrageNamenew">(Update only) new ISBN:</label>
                <input type="text" id="ouvrageNamenew" name="ouvrageNamenew" >

                <label for="ouvrageTitre">Titre:</label>
                <input type="text" id="ouvrageTitre" name="ouvrageTitre" required>
                     <label for="ouvrageAuteur">Auteur:</label>
                <input type="text" id="ouvrageAuteur" name="ouvrageAuteur" required>

                <button class="buttonq" type="button" onclick="setAction1('add')">Ajouter Ouvrage</button>
                <button class="buttonq" type="button" onclick="setAction1('update')">Modifier Ouvrage</button>
                <button class="buttonq" type="button" onclick="setAction1('delete')">Supprimer Ouvrage (par ISBN)</button>
            </form>

           <h2>Liste des Ouvrages</h2>
          <%
          String url = "jdbc:mysql://localhost:3306/mc_projet";
          String user = "root";
          String pwd = "";

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, user, pwd);

    PreparedStatement pst = con.prepareStatement("SELECT * FROM ouvrage");
    ResultSet rs = pst.executeQuery();
%>

<table border="1" width="100%">
    <tr>
        <th>ID</th>
        <th>ISBN</th>
        <th>Titre</th>
        <th>Auteur</th>
    </tr>

    <%
    while (rs.next()) {
        int id = rs.getInt("id");
        String ISBN = rs.getString("ISBN");
        String titre = rs.getString("titre");
        String auteur = rs.getString("auteur");

    %>
    <tr>
        <td><%=id %></td>
        <td><%=ISBN %></td>
        <td><%=titre %></td>
        <td><%=auteur %></td>
    </tr>
    <%
    }
    rs.close();
    pst.close();
    con.close();
} catch (SQLException | ClassNotFoundException ex) {
    ex.printStackTrace();
}
%>
</table>

        </div>
<!--//////////////////////////////////  exemplaire-->

  <div class="contentq" id="exemplairePage" style="display: none;">
            <h1>Gestion d'exemplaires</h1>
            <form class="formq" id="exemplaireForm" action="Exemplaire" method="post">
              <input type="hidden" name="action1" id="action1" value="" />
                  <label for="NumEx">Numéro d'exemplaire:</label>
        <input type="text" id="NumEx" name="NumEx" required>
        
           <label for="NumExnew">(for update only) new Numéro d'exemplaire:</label>
        <input type="text" id="NumExnew" name="NumExnew" >
        
     <label for="etat">Etat:</label>
<select id="etat" name="etat" class="select" required>
  <option value="disponible" selected>Disponible</option>
  <option value="indisponible">Indisponible</option>
</select>

        <label for="ISBN">ISBN:</label> 
        <input type="text" id="ISBN" name="ISBN" required>

        <label for="CodeRayon">Code Rayon:</label>
        <input type="text" id="CodeRayon" name="CodeRayon" required>


                <button class="buttonq" type="button" onclick="setAction2('add')">Ajouter Exemplaire</button>
                <button class="buttonq" type="button" onclick="setAction2('update')">Modifier Exemplaire</button>
                <button class="buttonq" type="button" onclick="setAction2('delete')">Supprimer Exemplaire (par numéro d'exemplaire)</button>
            </form>


  <h2>Listes des Exemplaires</h2>
           <%

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, user, pwd);

    PreparedStatement pst = con.prepareStatement("SELECT exemplaire.id, exemplaire.num_ex, exemplaire.etat, ouvrage.ISBN, ouvrage.titre, ouvrage.auteur, rayon.code_rayon, rayon.num_catalogue " +
            "FROM exemplaire " +
            "JOIN ouvrage ON exemplaire.ISBN = ouvrage.ISBN " +
            "JOIN rayon ON exemplaire.code_rayon = rayon.code_rayon;");
    ResultSet rs = pst.executeQuery();
%>

<table border="1" width="100%">
    <tr>
        <th>ID</th>
        <th>Num ex</th>
        <th>Etat</th>
        <th>ISBN</th>
        <th>Titre</th>
        <th>Auteur</th>
        <th>Code rayon</th>
        <th>Num catalogue</th>
    </tr>

    <%
    while (rs.next()) {
        int id = rs.getInt("id");
        String num_ex = rs.getString("num_ex");
        String etat = rs.getString("etat");
        String ISBN = rs.getString("ISBN");
        String titre = rs.getString("titre");
        String auteur = rs.getString("auteur");
        String code_rayon = rs.getString("code_rayon");
        String num_catalogue = rs.getString("num_catalogue");

    %>
    <tr>
        <td><%=id %></td>
        <td><%=num_ex %></td>
        <td><%=etat %></td>
        <td><%=ISBN %></td>
        <td><%=titre %></td>
        <td><%=auteur %></td>
        <td><%=code_rayon %></td>
        <td><%=num_catalogue %></td>
    </tr>
    <%
    }
    rs.close();
    pst.close();
    con.close();
} catch (SQLException | ClassNotFoundException ex) {
    ex.printStackTrace();
    // Handle the exception (e.g., show an error message)
    // JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}
%>
</table>


  <%
       

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, user, pwd);

    PreparedStatement pst = con.prepareStatement("SELECT * FROM ouvrage");
    ResultSet rs = pst.executeQuery();
%>
<div class="row flex">
<div class="col-md-6">
<h2>Listes d'Ouvrages</h2>
<table border="1" width="50%" >

    <tr>
        <th>ID</th>
        <th>ISBN</th>
        <th>Titre</th>
        <th>Auteur</th>
    </tr>

    <%
    while (rs.next()) {
        int id = rs.getInt("id");
        String ISBN = rs.getString("ISBN");
        String titre = rs.getString("titre");
        String auteur = rs.getString("auteur");

    %>
    <tr>
        <td><%=id %></td>
        <td><%=ISBN %></td>
        <td><%=titre %></td>
        <td><%=auteur %></td>
    </tr>
    <%
    }
    rs.close();
    pst.close();
    con.close();
} catch (SQLException | ClassNotFoundException ex) {
    ex.printStackTrace();
}
%>
</table>
</div>
      
       <%


try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, user, pwd);

    PreparedStatement pst = con.prepareStatement("SELECT * FROM rayon");
    ResultSet rs = pst.executeQuery();
%>
<div class="col-md-6">
<h2>Listes des Rayons</h2>
<table border="1" width="50%" >
    <tr>
        <th>ID</th>
        <th>Code_rayon</th>
    </tr>

    <%
    while (rs.next()) {
        int id = rs.getInt("id");
        String codeRayon = rs.getString("code_rayon");
    %>
    <tr>
        <td><%=id %></td>
        <td><%=codeRayon %></td>
    </tr>
    <%
    }
    rs.close();
    pst.close();
    con.close();
} catch (SQLException | ClassNotFoundException ex) {
    ex.printStackTrace();
}
%>
</table>
</div>
</div>
            <!-- Table to Display List of Catalogues -->
          
        </div>



<!-- ///////////////////////// -->   

<!--//////////////////////////////////  rayon-->

  <div class="contentq" id="rayonPage" style="display: none;">
            <h1>Gestion des Rayons</h1>
   

            
            <form class="formq" id="rayonForm" action="Rayon" method="post">
            
<input type="hidden" name="action2" id="action2" value="" />
        <label for="CodeRayon">Code Rayon:</label>
        <input type="text" id="CodeRayon" name="CodeRayon" required>
          <label for="UpdatedValue">(Only for update)UpdatedValue:</label>
        <input type="text" id="UpdatedValue" name="UpdatedValue" >


              <button class="buttonq" type="button" onclick="setAction('add')">Ajouter Rayon</button>
        <button class="buttonq" type="button" onclick="setAction('update')">Modifier Rayon</button>
        <button class="buttonq" type="button" onclick="setAction('delete')">Supprimer Rayon</button>
            </form>

         
            
       <%


try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, user, pwd);

    PreparedStatement pst = con.prepareStatement("SELECT * FROM rayon");
    ResultSet rs = pst.executeQuery();
%>
<h2>Listes des Rayons</h2>
<table border="1" width="100%">
    <tr>
        <th>ID</th>
        <th>Code_rayon</th>
    </tr>

    <%
    while (rs.next()) {
        int id = rs.getInt("id");
        String codeRayon = rs.getString("code_rayon");
    %>
    <tr>
        <td><%=id %></td>
        <td><%=codeRayon %></td>
    </tr>
    <%
    }
    rs.close();
    pst.close();
    con.close();
} catch (SQLException | ClassNotFoundException ex) {
    ex.printStackTrace();
}
%>
</table>

        </div>



<!-- ///////////////////////// --> 


     <div class="contentq" id="aquisitionPage" style="display: none;">
            <h1>Acquisition Des Ouvrages</h1>
            <form class="formq" id="acquisitionForm" action="Acquisition" method="post">
            <input type="hidden" name="action3" id="action3" value="" />
                <label for="ISBN">ISBN:</label>
                <input type="text" id="ISBN" name="ISBN" required>
                <label for="ISBNnew">(update only)new ISBN:</label>
                <input type="text" id="ISBNnew" name="ISBNnew" >
                <label for="Titre">Titre:</label>
                <input type="text" id="Titre" name="Titre" required>
                <label for="Auteur">ID Auteur:</label>
                <input type="text" id="Auteur" name="Auteur" required>
                <button class="buttonq" type="button" onclick="setAction3('add')">Ajouter Ouvrage</button>
                <button class="buttonq" type="button" onclick="setAction3('update')">Modifier Ouvrage</button>
                <button class="buttonq" type="button" onclick="setAction3('delete')">Supprimer Ouvrage (par ISBN)</button>
            </form>

            <!-- Table to Display List of "Ouvrages" -->
            
            
              <h2>Liste des Ouvrages à Acquérir</h2>
          <%
       

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, user, pwd);

    PreparedStatement pst = con.prepareStatement("SELECT * FROM acquerir_livre");
    ResultSet rs = pst.executeQuery();
%>

<table border="1" width="100%">
    <tr>
        <th>ID</th>
        <th>ISBN</th>
        <th>Titre</th>
        <th>Auteur</th>
    </tr>

    <%
    while (rs.next()) {
        int id = rs.getInt("id");
        String ISBN = rs.getString("ISBN");
        String titre = rs.getString("titre");
        String auteur = rs.getString("auteur");

    %>
    <tr>
        <td><%=id %></td>
        <td><%=ISBN %></td>
        <td><%=titre %></td>
        <td><%=auteur %></td>
    </tr>
    <%
    }
    rs.close();
    pst.close();
    con.close();
} catch (SQLException | ClassNotFoundException ex) {
    ex.printStackTrace();
}
%>
</table>

        </div>
            
            
       














        <div class="contentq" id="gempruntPage" style="display: none;" >
            <%@ page import="java.sql.*" %>
                  <%@ page import="java.text.SimpleDateFormat" %>
                        <%@ page import="java.util.Date" %>

<%



try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, user, pwd);

    // First Query
    PreparedStatement pst1 = con.prepareStatement("SELECT e.date_emprunt, e.date_restitution, u.id, u.nom, u.prenom, u.date_naiss, u.universite, u.role, u.matricule, u.date_penalisation, u.grade, u.annee_etude, u.payer, u.nombre_emprunts, ex.num_ex, o.ISBN, o.titre, o.auteur, r.code_rayon " +
            "FROM emprunts e " +
            "JOIN utilisateurs u ON e.id = u.id " +
            "JOIN exemplaire ex ON e.num_ex = ex.num_ex " +
            "JOIN ouvrage o ON ex.ISBN = o.ISBN " +
            "JOIN rayon r ON ex.code_rayon = r.code_rayon ");
    ResultSet rs1 = pst1.executeQuery();
%>
<div id="tableau1">
<h2 class="text-center">Rapport des Emprunts Générale</h2>
<table border="1" width="100%">
    <tr>
        <th>Date Emprunt</th>
        <th>Date Restitution</th>
        <th>ID</th>
        <th>Nom</th>
        <th>Prenom</th>
        <th>Date Naissance</th>
        <th>Universite</th>
        <th>Role</th>
        <th>Matricule</th>
        <th>Date Penalisation</th>
        <th   class="print-button">Action</th>
       
        <!-- ... other columns ... -->
    </tr>

    <%
    while (rs1.next()) {
    %>
    <tr>
        <td><%=rs1.getString("date_emprunt") %></td>
        <td><%=rs1.getString("date_restitution") %></td>
        <td><%=rs1.getString("id") %></td>
        <td><%=rs1.getString("nom") %></td>
        <td><%=rs1.getString("prenom") %></td>
        <td><%=rs1.getString("date_naiss") %></td>
        <td><%=rs1.getString("universite") %></td>
        <td><%=rs1.getString("role") %></td>
        <td><%=rs1.getString("matricule") %></td>
        <td><%=rs1.getString("date_penalisation") %></td>
    <td class="print-button" ><form action="Prolonger" method="post" onsubmit="return showConfirmation()">
                                        <input type="hidden" name="id" value="<%= rs1.getString("id") %>">
                                        <input type="hidden" name="num_ex" value="<%= rs1.getString("num_ex") %>">
                                        <button type="submit" class="btn btn-outline-warning">PROLONGER</button>
                                    </form>
                                    </td>
                                   
        <!-- ... other columns ... -->
    </tr>
    <%
    }
    rs1.close();
    pst1.close();
%>
</table>

<table border="1" width="100%">
<tr>

 <th>Grade</th>
        <th>Annee Etude</th>
        <th>Payer</th>
        <th>Nombre Emprunts</th>
        <th>Num Exemplaire</th>
        <th>ISBN</th>
        <th>Titre</th>
        <th>Auteur</th>
        <th>Code Rayon</th>

</tr>
<%  pst1 = con.prepareStatement("SELECT e.date_emprunt, e.date_restitution, u.id, u.nom, u.prenom, u.date_naiss, u.universite, u.role, u.matricule, u.date_penalisation, u.grade, u.annee_etude, u.payer, u.nombre_emprunts, ex.num_ex, o.ISBN, o.titre, o.auteur, r.code_rayon " +
        "FROM emprunts e " +
        "JOIN utilisateurs u ON e.id = u.id " +
        "JOIN exemplaire ex ON e.num_ex = ex.num_ex " +
        "JOIN ouvrage o ON ex.ISBN = o.ISBN " +
        "JOIN rayon r ON ex.code_rayon = r.code_rayon ");
 rs1 = pst1.executeQuery(); %>
 <%
    while (rs1.next()) {
    %>
    <tr>
        <td><%=rs1.getString("grade") %></td>
        <td><%=rs1.getString("annee_etude") %></td>
        <td><%=rs1.getString("payer") %></td>
        <td><%=rs1.getString("nombre_emprunts") %></td>
        <td><%=rs1.getString("num_ex") %></td>
        <td><%=rs1.getString("ISBN") %></td>
        <td><%=rs1.getString("titre") %></td>
        <td><%=rs1.getString("auteur") %></td>
        <td><%=rs1.getString("code_rayon") %></td>
        <!-- ... other columns ... -->
    </tr>
    <%
    }
    rs1.close();
    pst1.close();
    con.close();
}catch (Exception e) {
    System.out.print(e);
}
%>

</table>
</div>
 <button onclick="printTable('tableau1')" class="btn btn-outline-primary my-4">Imprimer le rapport générale</button>

<%
// Second Query

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, user, pwd);
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
String currentDate = dateFormat.format(new Date());
PreparedStatement pst2 = con.prepareStatement("SELECT e.date_emprunt, e.date_restitution,u.id, u.nom, u.prenom, u.date_naiss, u.universite, u.role, u.matricule, u.date_penalisation, u.grade, u.annee_etude, u.payer, u.nombre_emprunts, ex.num_ex, o.ISBN, o.titre, o.auteur, r.code_rayon " +
        "FROM emprunts e " +
        "JOIN utilisateurs u ON e.id = u.id " +
        "JOIN exemplaire ex ON e.num_ex = ex.num_ex " +
        "JOIN ouvrage o ON ex.ISBN = o.ISBN " +
        "JOIN rayon r ON ex.code_rayon = r.code_rayon " +
        "WHERE e.date_emprunt = '" + currentDate + "'");
ResultSet rs2 = pst2.executeQuery();
%>

<h2 class="text-center">Rapport des Emprunts du Jour</h2>
<div  id= "tableau2"   style="overflow-x:auto;">
   <table border="1" width="100%">
    <tr>
        <th>Date Emprunt</th>
        <th>Date Restitution</th>
        <th>ID</th>
        <th>Nom</th>
        <th>Prenom</th>
        <th>Date Naissance</th>
        <th>Universite</th>
        <th>Role</th>
        <th>Matricule</th>
        <th>Date Penalisation</th>
       
        <!-- ... other columns ... -->
    </tr>

    <%
    while (rs2.next()) {
    %>
    <tr>
        <td><%=rs2.getString("date_emprunt") %></td>
        <td><%=rs2.getString("date_restitution") %></td>
        <td><%=rs2.getString("id") %></td>
        <td><%=rs2.getString("nom") %></td>
        <td><%=rs2.getString("prenom") %></td>
        <td><%=rs2.getString("date_naiss") %></td>
        <td><%=rs2.getString("universite") %></td>
        <td><%=rs2.getString("role") %></td>
        <td><%=rs2.getString("matricule") %></td>
        <td><%=rs2.getString("date_penalisation") %></td>
      

        <!-- ... other columns ... -->
    </tr>
    <%
    }
    rs2.close();
    pst2.close();
%>
</table>

<table border="1" width="100%">
<tr>

 <th>Grade</th>
        <th>Annee Etude</th>
        <th>Payer</th>
        <th>Nombre Emprunts</th>
        <th>Num Exemplaire</th>
        <th>ISBN</th>
        <th>Titre</th>
        <th>Auteur</th>
        <th>Code Rayon</th>

</tr>
<%  pst2 = con.prepareStatement("SELECT e.date_emprunt, e.date_restitution,u.id, u.nom, u.prenom, u.date_naiss, u.universite, u.role, u.matricule, u.date_penalisation, u.grade, u.annee_etude, u.payer, u.nombre_emprunts, ex.num_ex, o.ISBN, o.titre, o.auteur, r.code_rayon " +
        "FROM emprunts e " +
        "JOIN utilisateurs u ON e.id = u.id " +
        "JOIN exemplaire ex ON e.num_ex = ex.num_ex " +
        "JOIN ouvrage o ON ex.ISBN = o.ISBN " +
        "JOIN rayon r ON ex.code_rayon = r.code_rayon " +
        "WHERE e.date_emprunt = '" + currentDate + "'");
rs2 = pst2.executeQuery();%>
 <%
    while (rs2.next()) {
    %>
    <tr>
        <td><%=rs2.getString("grade") %></td>
        <td><%=rs2.getString("annee_etude") %></td>
        <td><%=rs2.getString("payer") %></td>
        <td><%=rs2.getString("nombre_emprunts") %></td>
        <td><%=rs2.getString("num_ex") %></td>
        <td><%=rs2.getString("ISBN") %></td>
        <td><%=rs2.getString("titre") %></td>
        <td><%=rs2.getString("auteur") %></td>
        <td><%=rs2.getString("code_rayon") %></td>
        <!-- ... other columns ... -->
    </tr>
    <%
    }
    rs2.close();
    pst2.close();
    con.close();
}catch (Exception e) {
    System.out.print(e);
}
%>

</table>

</div>
 <button onclick="printTable('tableau2')" class="btn btn-outline-primary my-4">Imprimer le rapport de jour</button>
 
 
  
 
        </div>
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

        <div class="contentq" id="rempruntPage" style="display: none;">
            <h1>Restitution des Emprunts</h1>
            <form class="formq" id="rempruntForm" action="Restituer" method="post">
                       <label for="NumEx">Numéro d'exemplaire:</label>
        <input type="text" id="NumEx" name="NumEx" required>
        
        <label for="ISBN">ISBN:</label> 
        <input type="text" id="ISBN" name="ISBN" required>
        
                <label for="matricule">Matricule:</label> 
        <input type="text" id="matricule" name="matricule" required>
        


                <button class="buttonq" type="submit">Restituer un Ouvrage</button>
              
            </form>

        
         
        </div>

        
    </div>
    
    
      
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
    <script>
    function hideButtonOnPrint() {
        var printButton = document.querySelector('.print-button');
        if (window.matchMedia && window.matchMedia('print').matches) {
            // The page is being printed, hide the button
            printButton.style.display = 'none';
        }
    }

    // Call the function when the page loads
    window.addEventListener('load', hideButtonOnPrint);
    
    
    function showConfirmation() {
        // Customize your confirmation message and appearance
        var confirmationMessage = "Are you sure you want to prolonge the date?";
        var confirmationTitle = "Confirmation"; // Custom title (optional)

        // Use a custom-styled dialog or the browser's default confirm
        var userConfirmed = confirm(confirmationMessage);

        // Return true to proceed with the form submission or false to cancel
        return userConfirmed;
    }

    
    $(document).ready(function () {
        setTimeout(function () {
            $("#errorMessage, #successMessage").fadeOut(500);
        }, 3000);
    });
    function setAction(action) {
        var confirmation = confirm("Are you sure you want to perform this action?");
        
        if (confirmation) {
            document.getElementById('action2').value = action;

            // Check form validity before submitting
            if (document.getElementById('rayonForm').checkValidity()) {
                document.getElementById('rayonForm').submit();
            } else {
                alert("Please fill in all required fields .");
            }
        }
    }

    function setAction3(action) {
        var confirmation = confirm("Are you sure you want to perform this action?");
        
        if (confirmation) {
            document.getElementById('action3').value = action;

            // Check form validity before submitting
            if (document.getElementById('acquisitionForm').checkValidity()) {
                document.getElementById('acquisitionForm').submit();
            } else {
                alert("Please fill in all required fields.");
            }
        }
    }

    function setAction1(action) {
    	   var confirmation = confirm("Are you sure you want to perform this action?");
           
    	 if (confirmation) {
    	        document.getElementById('action').value = action;
    	        
    	        // Check form validity before submitting
    	        if (document.getElementById('ouvrageForm').checkValidity()) {
    	            document.getElementById('ouvrageForm').submit();
    	        } else {
    	            alert("Please fill in all required fields.");
    	        }
    	    }
    }
    
    function setAction2(action1) {
 	   var confirmation = confirm("Are you sure you want to perform this action?");
    	
 	  if (confirmation) {
	       
        document.getElementById("action1").value = action1;
        if (document.getElementById('exemplaireForm').checkValidity()) {
        
        document.getElementById("exemplaireForm").submit();
    }else {
        alert("Please fill in all required fields.");
    }}}
        function showPage(pageId) {
            // Hide all content sections
            document.querySelectorAll('.contentq').forEach(function (content) {
                content.style.display = 'none';
            });

            // Show the selected content section
            document.getElementById(pageId + 'Page').style.display = 'block';
        }
        
        
        function printTable(tableId) {
            var printContents = document.getElementById(tableId).innerHTML;
            var originalContents = document.body.innerHTML;
            document.body.innerHTML = printContents;
            window.print();
            document.body.innerHTML = originalContents;
        }
    </script>
    
<script  src="bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"></script>

</body>

</html>