package control;

public class EspaceAdherentController {

    public String handleRequest(String role, String searchInput) {
        String result = "";

        if ("Enseignant".equals(role) || "Etudiant".equals(role) || "EtudiantExterne".equals(role)) {
            result += "User role is valid. "; // Simulated logic

            // Database-related logic (simplified for the example)
            if (searchInput != null && !searchInput.isEmpty()) {
                result += "Search input is not empty. "; // Simulated logic
            }
        } else {
            result += "Access Denied. "; // Simulated logic
        }

        return result;
    }
}
