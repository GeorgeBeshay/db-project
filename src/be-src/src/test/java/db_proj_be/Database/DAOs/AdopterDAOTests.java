package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Adopter;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

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
public class AdopterDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AdopterDAO adopterDAO;

    @BeforeEach
    public void initAdopterDAOTest() {
        // enforce independence between the tests.
        this.adopterDAO = new AdopterDAO(jdbcTemplate);

        // checking for the absence of the id that is being used by the tests from the current DB state.
        assert adopterDAO.findById(10000) == null : "Current DB State contains an Adopter with ID 10_000, which is used in testing ..";
    }

    @AfterAll
    public void finishTests() {
        Logger.logMsgFrom(this.getClass().getName(), "AdopterDAOTests finished.", -1);
    }

    // ------------------------- Creation Tests -------------------------

    @Test
    @DisplayName("Adopter DAO Tests - Creation: Passing an object that contains all of the attributes set.")
    public void testAdopterCreationAllArguments() {
        // Arrange
        int id = -100; // will be ignored
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String phone = "123-456-7890";
        String birthDate = "1990-01-01";
        boolean gender = true;
        String address = "123 Main St, City";

        Adopter adopter = new Adopter(id, email, passwordHash, firstName, lastName, phone, birthDate, gender, address);

        // Act
        id = adopterDAO.create(adopter);

        // Assert
        assertTrue(id >= 1);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", id);
    }

    @Test
    @DisplayName("Adopter DAO Tests - Creation: Passing an object that contains the required attributes only set.")
    public void testAdopterCreationRequiredAttributesOnly() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        // Act
        int id = adopterDAO.create(adopter);

        // Assert
        assertTrue(id >= 1);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", id);
    }

    @Test
    @DisplayName("Adopter DAO Tests - Creation: Passing a duplicate object.")
    public void testAdopterCreationDuplicateObjects() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        // Act
        int id = adopterDAO.create(adopter);
        assertTrue(id >= 1);

        int id2 = adopterDAO.create(adopter);       // duplicate email.

        // Assert
        assertFalse(id2 >= 1);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", id);
    }

    @Test
    @DisplayName("Adopter DAO Tests - Creation: Passing an object that contains required attributes not set.")
    public void testAdopterCreationMissingAttributes() {
        // Arrange
        String firstName = null;
        String lastName = null;
        String email = null;
        String passwordHash = null;
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        // Act
        int id = adopterDAO.create(adopter);

        // Assert
        assertFalse(id >= 1);
    }

    @Test
    @DisplayName("Adopter DAO Tests - Creation: Passing a null object.")
    public void testAdopterCreationNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adopterDAO.create(null));
    }

    // ------------------------- Deletion Tests -------------------------

    @Test
    @DisplayName("Adopter DAO Tests - Deletion: Passing a null object.")
    public void testAdopterDeletionNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adopterDAO.delete(null));
    }

    @Test
    @DisplayName("Adopter DAO Tests - Deletion: Passing an object that doesn't exist in the current DB state.")
    public void testAdopterDeletionObjectDoesntExist() {
        // Arrange - none
        Adopter adopter = new Adopter();
        adopter.setId(10_000);

        // Act
        boolean isSuccess = adopterDAO.delete(adopter);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Adopter DAO Tests - Deletion: Passing a valid object")
    public void testAdopterDeletionValidObject() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        // Add the record to the DB
        int id = adopterDAO.create(adopter);
        assertTrue(id >= 1);
        adopter.setId(id);

        // Act
        boolean isSuccess = adopterDAO.delete(adopter);

        // Assert
        assertTrue(isSuccess);
    }

    // ------------------------- Updating Tests -------------------------

    @Test
    @DisplayName("Adopter DAO Tests - Updating: Passing a null object.")
    public void testAdopterUpdatingNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adopterDAO.update(null));
    }

    @Test
    @DisplayName("Adopter DAO Tests - Updating: Passing an object that doesn't exist in the current DB state.")
    public void testAdopterUpdatingObjectDoesntExist() {
        // Arrange - none
        Adopter adopter = new Adopter();
        adopter.setId(10_000);

        // Act
        boolean isSuccess = adopterDAO.update(adopter);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Adopter DAO Tests - Updating: Passing a valid object")
    public void testAdopterUpdatingValidObject() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        // Add the record to the DB
        int id = adopterDAO.create(adopter);
        assertTrue(id >= 1);
        adopter.setId(id);

        String modifiedAddress = "86 Main St, Alexandria";
        adopter.setAddress(modifiedAddress);

        // Act
        boolean isSuccess = adopterDAO.update(adopter);
        Adopter fetchedAdopter = adopterDAO.findById(id);

        // Assert
        assertTrue(isSuccess);
        assertEquals(fetchedAdopter.getAddress(), modifiedAddress);

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", id);
    }

    // ------------------------- Find Tests -------------------------

    @Test
    @DisplayName("Adopter DAO Tests - Find: Passing an id that doesn't exist")
    public void testAdopterFindIdDoesntExist() {
        // Arrange - none
        int id = 10_000;

        // Act & Assert
        assertNull(adopterDAO.findById(id));
    }

    @Test
    @DisplayName("Adopter DAO Tests - Find: Passing a valid id")
    public void testAdopterFindIdValid() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        // Add the record to the DB
        int id = adopterDAO.create(adopter);
        assertTrue(id >= 1);
        adopter.setId(id);

        // Act
        Adopter fetchedAdopter = adopterDAO.findById(id);

        // Assert
        assertEquals(adopter, fetchedAdopter);

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", id);
    }

    @Test
    @DisplayName("Adopter DAO Tests - Find All")
    public void testAdopterFindAll() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        String firstName2 = "John";
        String lastName2 = "Doe";
        String email2 = "john.doe2@example.com";
        String passwordHash2 = "hashedpassword";
        String address2 = "123 Main St, City";

        Adopter adopter2 = new Adopter();
        adopter2.setFirstName(firstName2);
        adopter2.setLastName(lastName2);
        adopter2.setEmail(email2);
        adopter2.setPasswordHash(passwordHash2);
        adopter2.setAddress(address2);

        // Add the record to the DB
        int id = adopterDAO.create(adopter);
        assertTrue(id >= 1);
        adopter.setId(id);

        int id2 = adopterDAO.create(adopter2);
        assertTrue(id2 >= 1);
        adopter2.setId(id2);

        // Act
        List<Adopter> fetchedAdopters = adopterDAO.findAll();

        // Assert
        assertTrue(fetchedAdopters.size() >= 2);
        assertTrue(fetchedAdopters.contains(adopter));
        assertTrue(fetchedAdopters.contains(adopter2));

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", id);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", id2);
    }

}
