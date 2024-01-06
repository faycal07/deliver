package control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EspaceAdherentControllerTest {

    @Test
    public void testHandleRequest() {
        // Arrange
        EspaceAdherentController controller = new EspaceAdherentController();

        // Act
        String result = controller.handleRequest("Enseignant", "searchInput");

        // Assert
        assertTrue(result.contains("User role is valid"));
        assertTrue(result.contains("Search input is not empty"));
    }
}
