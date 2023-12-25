package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.PetAvailabilityNotification;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class PetAvailabilityNotificationDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
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

    // ------------------------- Creation Tests -------------------------

    @Test
    public void testPetAvailabilityNotificationCreationAllArguments() {
        // Arrange
        int petId = 30_000;
        int adopterId = 20_000;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Act
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);

        // Assert
        assertTrue(isSuccess);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE pet_id = ? and adopter_id = ?", petId, adopterId);
    }

    @Test
    public void testPetAvailabilityNotificationCreationDuplicateObjects() {
        // Arrange
        int petId = 30_000;
        int adopterId = 20_000;
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
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE pet_id = ? and adopter_id = ?", petId, adopterId);
    }

    @Test
    public void testPetAvailabilityNotificationCreationMissingAttributes() {
        // Arrange
        int petId = 20_003;
        int adopterId = 30_003;

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification();
        petAvailabilityNotification.setAdopterId(adopterId);
        petAvailabilityNotification.setPetId(petId);


        // Act
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);

        // Assert
        assertFalse(isSuccess);
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
        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification();
        petAvailabilityNotification.setPetId(20_004);
        petAvailabilityNotification.setAdopterId(30_004);

        // Act
        boolean isSuccess = petAvailabilityNotificationDao.delete(petAvailabilityNotification);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    public void testPetAvailabilityNotificationDeletionValidObject() {
        int petId = 30_005;
        int adopterId = 20_005;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);
        assertTrue(isSuccess);

        // Act
        isSuccess = petAvailabilityNotificationDao.delete(petAvailabilityNotification);

        // Assert
        assertTrue(isSuccess);
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
        int petId = 30_006;
        int adopterId = 20_006;
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
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE adopter_id = ? and pet_id = ?", adopterId, petId);
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
        int petId = 30_007;
        int adopterId = 20_007;
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
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE adopter_id = ? and pet_id = ?", adopterId, petId);
    }

    @Test
    public void testPetAvailabilityNotificationFindStatusValid() {
        // Arrange
        int petId = 30_009;
        int adopterId = 20_009;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);
        assertTrue(isSuccess);

        int petId2 = 30_010;
        int adopterId2 = 20_010;
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
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE adopter_id = ? and pet_id = ?", adopterId, petId);
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE adopter_id = ? and pet_id = ?", adopterId2, petId2);
    }

    @Test
    public void testPetAvailabilityNotificationFindAll() {
        // Arrange
        int petId = 30_011;
        int adopterId = 20_011;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = petAvailabilityNotificationDao.create(petAvailabilityNotification);
        assertTrue(isSuccess);

        int petId2 = 30_012;
        int adopterId2 = 20_012;
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
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE adopter_id = ? and pet_id = ?", adopterId, petId);
        jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE adopter_id = ? and pet_id = ?", adopterId2, petId2);
    }
}
