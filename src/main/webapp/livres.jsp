<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Livres</title>
    <link href="bib.css" type="text/css" rel="stylesheet">
    <link href="bootstrap-5.3.2-dist/css/bootstrap.min.css" type="text/css" rel="stylesheet">
    <link href="style.css" type="text/css" rel="stylesheet">
</head>
<body>

<jsp:include page="nav.jsp" />

<%
    String url = "jdbc:mysql://localhost:3306/mc_projet";
    String user = "root";
    String pwd = "";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pwd);

        String query = "SELECT e.num_ex, e.ISBN, o.titre, o.auteur FROM exemplaire e INNER JOIN ouvrage o ON e.ISBN = o.ISBN WHERE e.etat = 'disponible'";
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
%>

<!-- Display Search Results -->
<div class="row p-5  mx-1" style="margin-top: 4%; background-color: lightgray;" >
<h3 class="text-center ">Livres Disponibles:</h3>
    <div class="col-md-12 m-0">
    
        <div class="card bg-warning">
            <div class="card-body ">
                <table class="table table-striped table-hover bg-warning">
                    <thead>
                          <tr class="bg-warning">
                            <th  class="bg-warning">Numéro</th>
                            <th  class="bg-warning">ISBN</th>
                            <th  class="bg-warning">Titre</th>
                            <th  class="bg-warning">Auteur</th>
                            <th  class="bg-warning text-center">Action</th>
                        </tr>
                    </thead>
                    <tbody>
<%
    while (rs.next()) {
%>
                        <tr class="bg-warning">
                            <td  class="bg-warning"><%= rs.getString("num_ex") %></td>
                            <td  class="bg-warning"><%= rs.getString("ISBN") %></td>
                            <td  class="bg-warning"><%= rs.getString("titre") %></td>
                            <td  class="bg-warning"><%= rs.getString("auteur") %></td>
                            <td  class="bg-warning text-center">
                                <a href="login.jsp" class="btn btn-primary">EMPRUNTER</a>
                            </td>
                        </tr>
<%
    }
    rs.close();
    pst.close();
    con.close();
} catch (Exception e) {
    e.printStackTrace();
}
%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
