package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;

/*
 * Important Notes and Assumptions
 * ---------------------------------
 * The implementation within this test class assumes that the id 10_000
 * is used for testing, and so the DB state should never contain a record with this id,
 * in addition to testing using those ids will not modify the db state, but instead create a temp record during the
 * test processing, and then remove the record when terminate the test.
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class AdminDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AdminDAO adminDAO;

    @BeforeEach
    public void initAdminDAOTest() {
        this.adminDAO = new AdminDAO(jdbcTemplate);

        // checking for the absence of the id that is being used by the tests from the current DB state.
        assert adminDAO.findById(10000) == null : "Current DB State contains an Adopter with ID 10_000, which is used in testing ..";
    }

    @AfterAll
    public void finishTests() {
        Logger.logMsgFrom(this.getClass().getName(), "AdminDAOTests finished.", -1);
    }

    // ------------------------- Creation Tests -------------------------

    @Test
    @DisplayName("Admin DAO Tests - Creation: Passing an object that contains all of the attributes set.")
    public void testAdminCreationAllArguments() {
        // Arrange
        int id = 10_000; // ignored
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String phone = "123-456-7890";

        Admin admin = new Admin(id, firstName, lastName, email, phone, passwordHash);

        // Act
        id = adminDAO.create(admin);

        // Assert
        assertTrue(id >= 1);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADMINISTRATOR WHERE id = ?", id);
    }

    @Test
    @DisplayName("Admin DAO Tests - Creation: Passing an object that contains the required attributes only set.")
    public void testAdminCreationRequiredAttributesOnly() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";

        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);

        // Act
        int id = adminDAO.create(admin);

        // Assert
        assertTrue(id >= 1);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADMINISTRATOR WHERE id = ?", id);
    }

    @Test
    @DisplayName("Admin DAO Tests - Creation: Passing a duplicate object.")
    public void testAdminCreationDuplicateObjects() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";

        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);

        // Act
        int id = adminDAO.create(admin);
        assertTrue(id >= 1);
        admin.setId(id);

        int id2 = adminDAO.create(admin);

        // Assert
        assertFalse(id2 >= 1);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADMINISTRATOR WHERE id = ?", id);
    }

    @Test
    @DisplayName("Admin DAO Tests - Creation: Passing an object that contains required attributes not set.")
    public void testAdminCreationMissingAttributes() {
        // Arrange
        String firstName = null;
        String lastName = null;
        String email = null;
        String passwordHash = null;

        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);

        // Act
        int id = adminDAO.create(admin);

        // Assert
        assertFalse(id >= 1);
    }

    @Test
    @DisplayName("Admin DAO Tests - Creation: Passing a null object.")
    public void testAdminCreationNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adminDAO.create(null));
    }

    // ------------------------- Deletion Tests -------------------------

    @Test
    @DisplayName("Admin DAO Tests - Deletion: Passing a null object.")
    public void testAdminDeletionNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adminDAO.delete(null));
    }

    @Test
    @DisplayName("Admin DAO Tests - Deletion: Passing an object that doesn't exist in the current DB state.")
    public void testAdminDeletionObjectDoesntExist() {
        // Arrange - none
        Admin admin = new Admin();
        admin.setId(10_000);

        // Act
        boolean isSuccess = adminDAO.delete(admin);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Admin DAO Tests - Deletion: Passing a valid object")
    public void testAdminDeletionValidObject() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";

        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);

        // Add the record to the DB
        int id = adminDAO.create(admin);
        assertTrue(id >= 1);
        admin.setId(id);

        // Act
        boolean isSuccess = adminDAO.delete(admin);

        // Assert
        assertTrue(isSuccess);
    }

    // ------------------------- Updating Tests -------------------------

    @Test
    @DisplayName("Admin DAO Tests - Updating: Passing a null object.")
    public void testAdminUpdatingNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adminDAO.update(null));
    }

    @Test
    @DisplayName("Admin DAO Tests - Updating: Passing an object that doesn't exist in the current DB state.")
    public void testAdminUpdatingObjectDoesntExist() {
        // Arrange - none
        Admin admin = new Admin();
        admin.setId(10_000);

        // Act
        boolean isSuccess = adminDAO.update(admin);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Admin DAO Tests - Updating: Passing a valid object")
    public void testAdminUpdatingValidObject() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";

        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);

        // Add the record to the DB
        int id = adminDAO.create(admin);
        assertTrue(id >= 1);
        admin.setId(id);

        String modifiedFirstName = "Mark";
        admin.setFirstName(modifiedFirstName);

        // Act
        boolean isSuccess = adminDAO.update(admin);
        Admin fetchedAdmin = adminDAO.findById(id);

        // Assert
        assertTrue(isSuccess);
        assertEquals(fetchedAdmin.getFirstName(), modifiedFirstName);

        // clean
        jdbcTemplate.update("DELETE FROM ADMINISTRATOR WHERE id = ?", id);
    }

    // ------------------------- Find Tests -------------------------

    @Test
    @DisplayName("Admin DAO Tests - Find: Passing an id that doesn't exist")
    public void testAdminFindIdDoesntExist() {
        // Arrange - none
        int id = 10_000;

        // Act & Assert
        assertNull(adminDAO.findById(id));
    }

    @Test
    @DisplayName("Admin DAO Tests - Find: Passing a valid id")
    public void testAdminFindIdValid() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";

        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);

        // Add the record to the DB
        int id = adminDAO.create(admin);
        assertTrue(id >= 1);
        admin.setId(id);

        // Act
        Admin fetchedAdmin = adminDAO.findById(id);

        // Assert
        assertEquals(admin, fetchedAdmin);

        // clean
        jdbcTemplate.update("DELETE FROM ADMINISTRATOR WHERE id = ?", id);
    }

    @Test
    @DisplayName("Admin DAO Tests - Find All")
    public void testAdminFindAll() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";

        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPasswordHash(passwordHash);

        String firstName2 = "John";
        String lastName2 = "Doe";
        String email2 = "john.doe2@example.com";
        String passwordHash2 = "hashedpassword";

        Admin admin2 = new Admin();
        admin2.setFirstName(firstName2);
        admin2.setLastName(lastName2);
        admin2.setEmail(email2);
        admin2.setPasswordHash(passwordHash2);

        // Add the record to the DB
        int id = adminDAO.create(admin);
        assertTrue(id >= 1);
        admin.setId(id);

        int id2 = adminDAO.create(admin2);
        assertTrue(id2 >= 1);
        admin2.setId(id2);

        // Act
        List<Admin> fetchedAdmins = adminDAO.findAll();

        // Assert
        assertTrue(fetchedAdmins.size() >= 2);
        assertTrue(fetchedAdmins.contains(admin));
        assertTrue(fetchedAdmins.contains(admin2));

        // clean
        jdbcTemplate.update("DELETE FROM ADMINISTRATOR WHERE id = ?", id);
        jdbcTemplate.update("DELETE FROM ADMINISTRATOR WHERE id = ?", id2);
    }

}
