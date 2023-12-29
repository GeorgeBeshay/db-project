package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.Database.DAOs.AdminDAO;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class AdminServicesTests {

    private final JdbcTemplate jdbcTemplate;
    private AdminServices adminServices;
    private AdminDAO adminDAO;

    @Autowired
    public AdminServicesTests(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void setUpBeforeEach() {
        this.adminServices = new AdminServices(jdbcTemplate);
        this.adminDAO = new AdminDAO(jdbcTemplate);
    }

    // --------------------- Sign In Tests ---------------------

    @Test
    @DisplayName("Admin Services Tests - Passing a null object")
    public void testAdminSignInLogicWithNullObject() {
        // Arrange
        Admin admin = null;

        // Act
        Admin resultAdmin = this.adminServices.SignInLogic(admin);

        // Assert
        assertNull(resultAdmin);
    }

    @Test
    @DisplayName("Admin Services Tests - Successful Authentication")
    public void testAdminSignInLogicWithValidObject() {
        // Arrange
        int id = -100; // ignored
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String phone = "123-456-7890";

        Admin expectedAdmin = new Admin(id, firstName, lastName, email, phone, Hasher.hash(passwordHash));
        id = this.adminDAO.create(expectedAdmin);
        expectedAdmin.setPasswordHash(passwordHash);
        expectedAdmin.setId(id);
        assertTrue(id >= 1);

        // Act
        Admin actualAdmin = this.adminServices.SignInLogic(expectedAdmin);

        // Assert
        expectedAdmin.setPasswordHash(Hasher.hash(passwordHash));
        assertEquals(expectedAdmin, actualAdmin);

        // Clean
        assertTrue(this.adminDAO.delete(expectedAdmin));
    }

    @Test
    @DisplayName("Admin Services Tests - Authentication Failure")
    public void testAdminSignInLogicWithInvalidObject() {
        // Arrange
        int id = -100; // ignored
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String phone = "123-456-7890";

        Admin expectedAdmin = new Admin(id, firstName, lastName, email, phone, Hasher.hash(passwordHash));
        id = this.adminDAO.create(expectedAdmin);
        expectedAdmin.setPasswordHash(passwordHash + "_modified");
        expectedAdmin.setId(id);
        assertTrue(id >= 1);

        // Act
        Admin actualAdmin = this.adminServices.SignInLogic(expectedAdmin);

        // Assert
        assertNull(actualAdmin);

        // Clean
        assertTrue(this.adminDAO.delete(expectedAdmin));
    }


}
