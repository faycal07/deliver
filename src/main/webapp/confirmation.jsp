<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmation</title>
    
    <link href="bootstrap-5.3.2-dist/css/bootstrap.min.css" type="text/css" rel="stylesheet">
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
  
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <!-- Display the receipt content here with Bootstrap styling -->
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">University Béjaia</h3>
                </div>
                <div class="card-body">
                    <div>
                        <%= request.getAttribute("receiptContent") %>
                    </div>
                    <!-- Button to manually trigger the print action -->
                    <div class="text-center mt-3 print-button">
                        <button class="btn btn-primary" onclick="printReceipt()">Print Receipt</button>
                         <a class="btn btn-secondary" href="emprunter.jsp">Retour</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"></script>

<!-- JavaScript to manually print the receipt -->
<script type="text/javascript">
    function printReceipt() {
        // Use the window.print() method to trigger the print dialog
        window.print();
    }
    
    window.onload = function () {
        // Use a timeout to ensure that the print dialog appears after the content is loaded
        setTimeout(function () {
            window.print();
        }, 1000); // Adjust the delay as needed
    };
</script>

</body>
</html>
