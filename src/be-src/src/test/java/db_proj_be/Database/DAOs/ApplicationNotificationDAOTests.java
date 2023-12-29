package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.*;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class ApplicationNotificationDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ApplicationNotificationDAO applicationNotificationDAO;

    private int petId;
    private int adopterId;

    @BeforeAll
    public void prepareDatabase() {
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        this.petId = petDAO.create(pet);

        assertTrue(this.petId > 0);

        String firstName = "John";
        String lastName = "Doe";
        String email = "John.Doe@example.com";
        String passwordHash = "hashedpassword";
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        this.adopterId = adopterDAO.create(adopter);

        assertTrue(this.adopterId > 0);
    }

    @AfterAll
    public void clean() {
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", this.petId);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", this.adopterId);
    }

    @BeforeEach
    public void initAdopterDAOTest() {
        // enforce independence between the tests.
        this.applicationNotificationDAO = new ApplicationNotificationDAO(jdbcTemplate);
    }

    @AfterAll
    public void finishTests() {
        System.out.println("ApplicationNotificationDAOTests finished.");
    }

    // Creation methods for referenced entities to satisfy foreign key constraints
    private int createAdoptionApplication() {
        // Arrange
        int adopterId = this.adopterId;
        int petId = this.petId;
        ApplicationStatus status = ApplicationStatus.PENDING;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";

        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setAdopterId(adopterId);
        adoptionApplication.setPetId(petId);
        adoptionApplication.setStatus(status);
        adoptionApplication.setDescription(description);
        adoptionApplication.setExperience(experience);
        adoptionApplication.setCreationDate(creationDate);

        int appId = new AdoptionApplicationDAO(jdbcTemplate).create(adoptionApplication);
        assertTrue(appId > 0);
        return appId;
    }

    // ------------------------- Creation Tests -------------------------

    @Test
    public void testApplicationNotificationCreationAllArguments() {
        // Arrange
        int appId = this.createAdoptionApplication();
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Act
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertTrue(isSuccess);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
    }

    @Test
    public void testApplicationNotificationCreationDuplicateObjects() {
        // Arrange
        int appId = this.createAdoptionApplication();
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Act
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertTrue(isSuccess);
        isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertFalse(isSuccess);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
    }

    @Test
    public void testApplicationNotificationCreationMissingAttributes() {
        // Arrange
        int adopterId = this.adopterId;
        int appId = this.createAdoptionApplication();

        ApplicationNotification applicationNotification = new ApplicationNotification();
        applicationNotification.setAdopterId(adopterId);
        applicationNotification.setApplicationId(appId);


        // Act
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertFalse(isSuccess);

        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
    }

    @Test
    public void testApplicationNotificationCreationNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> applicationNotificationDAO.create(null));
    }

    // ------------------------- Deletion Tests -------------------------

    @Test
    public void testApplicationNotificationDeletionNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> applicationNotificationDAO.delete(null));
    }

    @Test
    public void testApplicationNotificationDeletionObjectDoesntExist() {
        // Arrange - none
        ApplicationNotification applicationNotification = new ApplicationNotification();

        int appId = this.createAdoptionApplication();

        applicationNotification.setApplicationId(appId);
        applicationNotification.setAdopterId(this.adopterId);

        // Act
        boolean isSuccess = applicationNotificationDAO.delete(applicationNotification);

        // Assert
        assertFalse(isSuccess);

        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
    }

    @Test
    public void testApplicationNotificationDeletionValidObject() {
        // Arrange
        int appId = this.createAdoptionApplication();
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        // Act
        isSuccess = applicationNotificationDAO.delete(applicationNotification);

        // Assert
        assertTrue(isSuccess);

        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
    }

    // ------------------------- Find Tests -------------------------

    @Test
    public void testApplicationNotificationFindAdopterIdDoesntExist() {
        // Arrange - none
        int adopterId = 20_008;

        // Act & Assert
        assertTrue(applicationNotificationDAO.findByAdopterId(adopterId).isEmpty());
    }

    @Test
    public void testApplicationNotificationFindAdopterIdValid() {
        int appId = this.createAdoptionApplication();
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        // Act
        List<ApplicationNotification> fetchedAppNot = applicationNotificationDAO.findByAdopterId(adopterId);

        // Assert
        assertTrue(fetchedAppNot.size() >= 1);
        assertTrue(fetchedAppNot.contains(applicationNotification));

        // clean
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
    }

    @Test
    public void testApplicationNotificationFindAppIdDoesntExist() {
        // Arrange - none
        int appId = 30_008;

        // Act & Assert
        assertTrue(applicationNotificationDAO.findByAppId(appId).isEmpty());
    }

    @Test
    public void testApplicationNotificationFindAppIdValid() {
        // Arrange
        int appId = this.createAdoptionApplication();
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        // Act
        List<ApplicationNotification> fetchedAppNot = applicationNotificationDAO.findByAppId(appId);

        // Assert
        assertTrue(fetchedAppNot.size() >= 1);
        assertTrue(fetchedAppNot.contains(applicationNotification));

        // clean
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId, appId);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
    }

    @Test
    public void testApplicationNotificationFindStatusValid() {
        // Arrange
        int appId = this.createAdoptionApplication();
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        int appId2 = this.createAdoptionApplication();
        int adopterId2 = this.adopterId;
        boolean status2 = true;
        Date date2 = Date.valueOf("2006-03-15");

        ApplicationNotification applicationNotification2 = new ApplicationNotification(appId2, adopterId2, status2, date2);

        // Add the record to DB
        isSuccess = applicationNotificationDAO.create(applicationNotification2);
        assertTrue(isSuccess);

        // Act
        List<ApplicationNotification> fetchedAppNot = applicationNotificationDAO.findByStatus(status);

        // Assert
        assertTrue(fetchedAppNot.size() >= 1);
        assertTrue(fetchedAppNot.contains(applicationNotification));

        fetchedAppNot = applicationNotificationDAO.findByStatus(status2);

        // Assert
        assertTrue(fetchedAppNot.size() >= 1);
        assertTrue(fetchedAppNot.contains(applicationNotification2));

        // clean
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId);
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId2);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId2);
    }

    @Test
    public void testApplicationNotificationFindAll() {
        // Arrange
        int appId = this.createAdoptionApplication();
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        int appId2 = this.createAdoptionApplication();
        int adopterId2 = this.adopterId;
        boolean status2 = true;
        Date date2 = Date.valueOf("2006-03-15");

        ApplicationNotification applicationNotification2 = new ApplicationNotification(appId2, adopterId2, status2, date2);

        // Add the record to DB
        isSuccess = applicationNotificationDAO.create(applicationNotification2);
        assertTrue(isSuccess);

        // Act
        List<ApplicationNotification> fetchedAppNot = applicationNotificationDAO.findAll();

        // Assert
        assertTrue(fetchedAppNot.size() >= 2);
        assertTrue(fetchedAppNot.contains(applicationNotification));
        assertTrue(fetchedAppNot.contains(applicationNotification2));

        // clean
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId);
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId2);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", appId2);
    }

}
