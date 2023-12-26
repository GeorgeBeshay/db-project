package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
import db_proj_be.BusinessLogic.EntityModels.ApplicationStatus;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Important Notes and Assumptions
 * ---------------------------------
 * The implementation within this test class assumes that the ids from 10_000 to 10_011 (inclusively)
 * besides to adopter ids from 20_000 to 20_011 and pet ids from 30_000 to 30_011 (inclusively)
 * are used for testing, and so the DB state should never contain a record containing any of those ids,
 * in addition to testing using this ids will not modify the db state, but instead create a temp record during the
 * test processing, and then remove the record when terminate the test.
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class AdoptionApplicationDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AdoptionApplicationDAO adoptionApplicationDAO;

    @BeforeEach
    public void initAdopterDAOTest() {
        // enforce independence between the tests.
        this.adoptionApplicationDAO = new AdoptionApplicationDAO(jdbcTemplate);
    }

    @AfterAll
    public void finishTests() {
        System.out.println("AdoptionApplicationDAOTests finished.");
    }

    // ------------------------- Creation Tests -------------------------

    @Test
    public void testAdoptionApplicationCreationAllArguments() {
        // Arrange
        int adopterId = 20_000;
        int pet_id = 30_000;
        ApplicationStatus status = ApplicationStatus.REJECTED;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";
        String closingDate = "2001-12-15";

        AdoptionApplication adoptionApplication = new AdoptionApplication(adopterId, pet_id, status, description, experience, creationDate, closingDate);

        // Act
        int id = adoptionApplicationDAO.create(adoptionApplication);

        // Assert
        assertTrue(id > 0);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
    }

    @Test
    public void testAdoptionApplicationCreationRequiredAttributesOnly() {
        // Arrange
        int adopterId = 20_001;
        int pet_id = 30_001;
        ApplicationStatus status = ApplicationStatus.PENDING;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";

        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setAdopterId(adopterId);
        adoptionApplication.setPetId(pet_id);
        adoptionApplication.setStatus(status);
        adoptionApplication.setDescription(description);
        adoptionApplication.setExperience(experience);
        adoptionApplication.setCreationDate(creationDate);

        // Act
        int id = adoptionApplicationDAO.create(adoptionApplication);

        // Assert
        assertTrue(id > 0);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
    }

    @Test
    public void testAdoptionApplicationCreationMissingAttributes() {
        // Arrange
        int adopterId = 20_003;
        int pet_id = 30_003;
        ApplicationStatus status = null;
        String description = null;
        Boolean experience = null;
        String creationDate = null;

        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setAdopterId(adopterId);
        adoptionApplication.setPetId(pet_id);
        adoptionApplication.setStatus(status);
        adoptionApplication.setDescription(description);
        adoptionApplication.setExperience(experience);
        adoptionApplication.setCreationDate(creationDate);

        // Act
        int id = adoptionApplicationDAO.create(adoptionApplication);

        // Assert
        assertFalse(id > 0);
    }

    @Test
    public void testAdoptionApplicationCreationNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adoptionApplicationDAO.create(null));
    }

    // ------------------------- Deletion Tests -------------------------

    @Test
    public void testAdoptionApplicationDeletionNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adoptionApplicationDAO.delete(null));
    }

    @Test
    public void testAdoptionApplicationDeletionObjectDoesntExist() {
        // Arrange - none
        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setId(10_004);

        // Act
        boolean isSuccess = adoptionApplicationDAO.delete(adoptionApplication);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    public void testAdoptionApplicationDeletionValidObject() {
        // Arrange
        int adopterId = 20_005;
        int pet_id = 30_005;
        ApplicationStatus status = ApplicationStatus.REJECTED;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";
        String closingDate = "2001-12-15";

        AdoptionApplication adoptionApplication = new AdoptionApplication(adopterId, pet_id, status, description, experience, creationDate, closingDate);


        // Add the record to the DB
        int id = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(id > 0);

        adoptionApplication.setId(id);

        // Act
        boolean isSuccess = adoptionApplicationDAO.delete(adoptionApplication);

        // Assert
        assertTrue(isSuccess);
    }

    // ------------------------- Updating Tests -------------------------

    @Test
    public void testAdoptionApplicationUpdatingNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adoptionApplicationDAO.update(null));
    }

    @Test
    public void testAdoptionApplicationUpdatingObjectDoesntExist() {
        // Arrange - none
        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setId(10_006);

        // Act
        boolean isSuccess = adoptionApplicationDAO.update(adoptionApplication);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    public void testAdoptionApplicationUpdatingValidObject() {
        // Arrange
        int adopterId = 20_007;
        int pet_id = 30_007;
        ApplicationStatus status = ApplicationStatus.PENDING;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";

        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setAdopterId(adopterId);
        adoptionApplication.setPetId(pet_id);
        adoptionApplication.setStatus(status);
        adoptionApplication.setDescription(description);
        adoptionApplication.setExperience(experience);
        adoptionApplication.setCreationDate(creationDate);

        // Add the record to the DB
        int id = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(id > 0);

        ApplicationStatus newStatus = ApplicationStatus.APPROVED;
        String closingDate = "2001-12-15";
        adoptionApplication.setId(id);
        adoptionApplication.setStatus(newStatus);
        adoptionApplication.setClosingDate(closingDate);

        // Act
        boolean isSuccess = adoptionApplicationDAO.update(adoptionApplication);
        AdoptionApplication fetchedAdoptionApp = adoptionApplicationDAO.findById(id);

        // Assert
        assertTrue(isSuccess);
        assertEquals(fetchedAdoptionApp.getStatus(), newStatus);
        assertEquals(fetchedAdoptionApp.getClosingDate(), closingDate);

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
    }

    // ------------------------- Find Tests -------------------------

    @Test
    public void testAdoptionApplicationFindAdopterIdDoesntExist() {
        // Arrange - none
        int adopterId = 20_008;

        // Act & Assert
        assertTrue(adoptionApplicationDAO.findByAdopterId(adopterId).isEmpty());
    }

    @Test
    public void testAdoptionApplicationFindAdopterIdValid() {
        // Arrange
        int adopterId = 20_009;
        int pet_id = 30_009;
        ApplicationStatus status = ApplicationStatus.REJECTED;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";
        String closingDate = "2001-12-15";

        AdoptionApplication adoptionApplication = new AdoptionApplication(adopterId, pet_id, status, description, experience, creationDate, closingDate);


        // Add the record to the DB
        int id = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(id > 0);

        // Act
        List<AdoptionApplication> fetchedAdoptionApps = adoptionApplicationDAO.findByAdopterId(adopterId);

        // Assert
        assertTrue(fetchedAdoptionApps.contains(adoptionApplication));

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
    }

    @Test
    public void testAdoptionApplicationFindPetIdDoesntExist() {
        // Arrange - none
        int petId = 30_008;

        // Act & Assert
        assertTrue(adoptionApplicationDAO.findByPetId(petId).isEmpty());
    }

    @Test
    public void testAdoptionApplicationFindPetIdValid() {
        // Arrange
        int adopterId = 20_009;
        int pet_id = 30_009;
        ApplicationStatus status = ApplicationStatus.REJECTED;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";
        String closingDate = "2001-12-15";

        AdoptionApplication adoptionApplication = new AdoptionApplication(adopterId, pet_id, status, description, experience, creationDate, closingDate);


        // Add the record to the DB
        int id = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(id > 0);

        adoptionApplication.setId(id);

        // Act
        List<AdoptionApplication> fetchedAdoptionApps = adoptionApplicationDAO.findByPetId(pet_id);

        // Assert
        assertTrue(fetchedAdoptionApps.contains(adoptionApplication));

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
    }

    @Test
    public void testAdoptionApplicationFindStatusValid() {
        // Arrange
        int adopterId1 = 20_010;
        int pet_id1 = 30_010;
        ApplicationStatus status1 = ApplicationStatus.REJECTED;
        String description1 = "I need to adopt this pet.";
        Boolean experience1 = false;
        String creationDate1 = "1990-12-13";
        String closingDate1 = "2001-12-15";

        AdoptionApplication adoptionApplication1 = new AdoptionApplication(adopterId1, pet_id1, status1, description1, experience1, creationDate1, closingDate1);

        int adopterId2 = 20_010;
        int pet_id2 = 30_011;
        ApplicationStatus status2 = ApplicationStatus.APPROVED;
        String description2 = "I love this pet.";
        Boolean experience2 = true;
        String creationDate2 = "1995-11-16";
        String closingDate2 = "2020-01-12";

        AdoptionApplication adoptionApplication2 = new AdoptionApplication(adopterId2, pet_id2, status2, description2, experience2, creationDate2, closingDate2);


        // Add the record to the DB
        int id1 = adoptionApplicationDAO.create(adoptionApplication1);
        assertTrue(id1 > 0);
        adoptionApplication1.setId(id1);

        int id2 = adoptionApplicationDAO.create(adoptionApplication2);
        assertTrue(id2 > 0);
        adoptionApplication2.setId(id2);

        // Act
        List<AdoptionApplication> fetchedAdoptionApps = adoptionApplicationDAO.findByStatus(ApplicationStatus.REJECTED);

        // Assert
        assertTrue(fetchedAdoptionApps.size() >= 1);
        assertTrue(fetchedAdoptionApps.contains(adoptionApplication1));

        // Act
        fetchedAdoptionApps = adoptionApplicationDAO.findByStatus(ApplicationStatus.APPROVED);

        // Assert
        assertTrue(fetchedAdoptionApps.size() >= 1);
        assertTrue(fetchedAdoptionApps.contains(adoptionApplication2));

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id1);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id2);
    }

    @Test
    public void testAdoptionApplicationFindAll() {
        // Arrange
        int adopterId1 = 20_010;
        int pet_id1 = 30_010;
        ApplicationStatus status1 = ApplicationStatus.REJECTED;
        String description1 = "I need to adopt this pet.";
        Boolean experience1 = false;
        String creationDate1 = "1990-12-13";
        String closingDate1 = "2001-12-15";

        AdoptionApplication adoptionApplication1 = new AdoptionApplication(adopterId1, pet_id1, status1, description1, experience1, creationDate1, closingDate1);

        int adopterId2 = 20_011;
        int pet_id2 = 30_011;
        ApplicationStatus status2 = ApplicationStatus.APPROVED;
        String description2 = "I love this pet.";
        Boolean experience2 = true;
        String creationDate2 = "1995-11-16";
        String closingDate2 = "2020-01-12";

        AdoptionApplication adoptionApplication2 = new AdoptionApplication(adopterId2, pet_id2, status2, description2, experience2, creationDate2, closingDate2);

        // Add the record to the DB
        int id1 = adoptionApplicationDAO.create(adoptionApplication1);
        assertTrue(id1 > 0);

        int id2 = adoptionApplicationDAO.create(adoptionApplication2);
        assertTrue(id2 > 0);

        // Act
        List<AdoptionApplication> fetchedApps = adoptionApplicationDAO.findAll();

        // Assert
        assertTrue(fetchedApps.size() >= 2);
        assertTrue(fetchedApps.contains(adoptionApplication1));
        assertTrue(fetchedApps.contains(adoptionApplication2));

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id1);
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id2);
    }

}
