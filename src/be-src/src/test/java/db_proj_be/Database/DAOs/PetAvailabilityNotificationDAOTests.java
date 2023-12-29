package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Adopter;
import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.EntityModels.PetAvailabilityNotification;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class PetAvailabilityNotificationDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private PetAvailabilityNotificationDAO petAvailabilityNotificationDao;

    @BeforeEach
    public void initAdopterDAOTest() {
        // enforce independence between the tests.
        this.petAvailabilityNotificationDao = new PetAvailabilityNotificationDAO(jdbcTemplate);
    }

    @AfterAll
    public void finishTests() {
        System.out.println("PetAvailabilityNotificationDAOTests finished.");
    }

    // Creation methods for referenced entities to satisfy foreign key constraints
    private int createAdopter(String email) {
        String firstName = "John";
        String lastName = "Doe";
        String passwordHash = "hashedpassword";
        String address = "123 Main St, City";

        Adopter adopter = new Adopter();
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setEmail(email);
        adopter.setPasswordHash(passwordHash);
        adopter.setAddress(address);

        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        int adopterId = adopterDAO.create(adopter);
        assertTrue(adopterId > 0);
        return adopterId;
    }

    private int createPet() {
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        int petId = petDAO.create(pet);

        assertTrue(petId > 0);
        return petId;
    }

    // helper method to clean database after each test
    private void clean(int petId, int adopterId) {
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE pet_id = ? and adopter_id = ?", petId, adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
    }

    // ------------------------- Creation Tests -------------------------

    @Test
    public void testPetAvailabilityNotificationCreationAllArguments() {
        // Arrange
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe31@example.com");
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Act
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);

        // Assert
        assertTrue(isSuccess);

        // Clean (to prevent modifying the DB current state)
        this.clean(petId, adopterId);
    }

    @Test
    public void testPetAvailabilityNotificationCreationDuplicateObjects() {
        // Arrange
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe32@example.com");
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Act
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);

        // Assert
        assertTrue(isSuccess);
        isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);

        // Assert
        assertFalse(isSuccess);

        // Clean (to prevent modifying the DB current state)
        this.clean(petId, adopterId);
    }

    @Test
    public void testPetAvailabilityNotificationCreationMissingAttributes() {
        // Arrange
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe33@example.com");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification();
        petAvailabilityNotification.setAdopterId(adopterId);
        petAvailabilityNotification.setPetId(petId);


        // Act
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);

        // Assert
        assertFalse(isSuccess);

        this.clean(petId, adopterId);
    }

    @Test
    public void testPetAvailabilityNotificationCreationNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> petAvailabilityNotificationDao.create(null));
    }

    // ------------------------- Deletion Tests -------------------------

    @Test
    public void testPetAvailabilityNotificationDeletionNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> petAvailabilityNotificationDao.delete(null));
    }

    @Test
    public void testPetAvailabilityNotificationDeletionObjectDoesntExist() {
        // Arrange - none
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe34@example.com");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification();
        petAvailabilityNotification.setPetId(petId);
        petAvailabilityNotification.setAdopterId(adopterId);

        // Act
        boolean isSuccess = petAvailabilityNotificationDao.delete(petAvailabilityNotification);

        // Assert
        assertFalse(isSuccess);

        // Clean
        this.clean(petId, adopterId);
    }

    @Test
    public void testPetAvailabilityNotificationDeletionValidObject() {
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe35@example.com");
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);
        assertTrue(isSuccess);

        // Act
        isSuccess = petAvailabilityNotificationDao.delete(petAvailabilityNotification);

        // Assert
        assertTrue(isSuccess);

        // Clean
        this.clean(petId, adopterId);
    }

    // ------------------------- Find Tests -------------------------

    @Test
    public void testPetAvailabilityNotificationFindAdopterIdDoesntExist() {
        // Arrange - none
        int adopterId = 20_008;

        // Act & Assert
        assertTrue(petAvailabilityNotificationDao.findByAdopterId(adopterId).isEmpty());
    }

    @Test
    public void testPetAvailabilityNotificationFindAdopterIdValid() {
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe36@example.com");
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);
        assertTrue(isSuccess);

        // Act
        List<PetAvailabilityNotification> fetchedNot = petAvailabilityNotificationDao.findByAdopterId(adopterId);

        // Assert
        assertTrue(fetchedNot.size() >= 1);
        assertTrue(fetchedNot.contains(petAvailabilityNotification));

        // clean
        this.clean(petId, adopterId);
    }

    @Test
    public void testPetAvailabilityNotificationFindAppIdDoesntExist() {
        // Arrange - none
        int petId = 30_008;

        // Act & Assert
        assertTrue(petAvailabilityNotificationDao.findByPetId(petId).isEmpty());
    }

    @Test
    public void testPetAvailabilityNotificationFindAppIdValid() {
        // Arrange
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe37@example.com");
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);
        assertTrue(isSuccess);

        // Act
        List<PetAvailabilityNotification> fetchedNot = petAvailabilityNotificationDao.findByPetId(petId);

        // Assert
        assertTrue(fetchedNot.size() >= 1);
        assertTrue(fetchedNot.contains(petAvailabilityNotification));

        // clean
        this.clean(petId, adopterId);
    }

    @Test
    public void testPetAvailabilityNotificationFindStatusValid() {
        // Arrange
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe38@example.com");
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);
        assertTrue(isSuccess);

        int petId2 = this.createPet();
        int adopterId2 = this.createAdopter("john.Doe39@example.com");
        boolean status2 = true;
        Date date2 = Date.valueOf("2006-03-15");

        PetAvailabilityNotification petAvailabilityNotification1 = new PetAvailabilityNotification(petId2, adopterId2, status2, date2);

        // Add the record to DB
        isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification1);
        assertTrue(isSuccess);

        // Act
        List<PetAvailabilityNotification> fetchedNot = petAvailabilityNotificationDao.findByStatus(status);

        // Assert
        assertTrue(fetchedNot.size() >= 1);
        assertTrue(fetchedNot.contains(petAvailabilityNotification));

        fetchedNot = petAvailabilityNotificationDao.findByStatus(status2);

        // Assert
        assertTrue(fetchedNot.size() >= 1);
        assertTrue(fetchedNot.contains(petAvailabilityNotification1));

        // clean
        this.clean(petId, adopterId);
        this.clean(petId2, adopterId2);
    }

    @Test
    public void testPetAvailabilityNotificationFindAll() {
        // Arrange
        int petId = this.createPet();
        int adopterId = this.createAdopter("john.Doe40@example.com");
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);
        assertTrue(isSuccess);

        int petId2 = this.createPet();
        int adopterId2 = this.createAdopter("john.Doe41@example.com");
        boolean status2 = true;
        Date date2 = Date.valueOf("2006-03-15");

        PetAvailabilityNotification petAvailabilityNotification2 = new PetAvailabilityNotification(petId2, adopterId2, status2, date2);

        // Add the record to DB
        isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification2);
        assertTrue(isSuccess);

        // Act
        List<PetAvailabilityNotification> fetchedNots = petAvailabilityNotificationDao.findAll();

        // Assert
        assertTrue(fetchedNots.size() >= 2);
        assertTrue(fetchedNots.contains(petAvailabilityNotification));
        assertTrue(fetchedNots.contains(petAvailabilityNotification2));

        // clean
        this.clean(petId, adopterId);
        this.clean(petId2, adopterId2);
    }
}
