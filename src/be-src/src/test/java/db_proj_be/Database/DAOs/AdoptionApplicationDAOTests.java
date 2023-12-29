package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Adopter;
import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
import db_proj_be.BusinessLogic.EntityModels.ApplicationStatus;
import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class AdoptionApplicationDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
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
        assertTrue(adopterId >= 1);
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

    // ------------------------- Creation Tests -------------------------

    @Test
    public void testAdoptionApplicationCreationAllArguments() {
        // Arrange
        int adopterId = this.createAdopter("john.Doe1@example.com");
        int petId = this.createPet();
        ApplicationStatus status = ApplicationStatus.REJECTED;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";
        String closingDate = "2001-12-15";

        AdoptionApplication adoptionApplication = new AdoptionApplication(adopterId, petId, status, description, experience, creationDate, closingDate);

        // Act
        int id = adoptionApplicationDAO.create(adoptionApplication);

        // Assert
        assertTrue(id > 0);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    public void testAdoptionApplicationCreationRequiredAttributesOnly() {
        // Arrange
        int adopterId = this.createAdopter("john.Doe2@example.com");
        int petId = this.createPet();
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

        // Act
        int id = adoptionApplicationDAO.create(adoptionApplication);

        // Assert
        assertTrue(id > 0);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    public void testAdoptionApplicationCreationMissingAttributes() {
        // Arrange
        int adopterId = this.createAdopter("john.Doe3@example.com");
        int petId = this.createPet();
        ApplicationStatus status = null;
        String description = null;
        Boolean experience = null;
        String creationDate = null;

        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setAdopterId(adopterId);
        adoptionApplication.setPetId(petId);
        adoptionApplication.setStatus(status);
        adoptionApplication.setDescription(description);
        adoptionApplication.setExperience(experience);
        adoptionApplication.setCreationDate(creationDate);

        // Act
        int id = adoptionApplicationDAO.create(adoptionApplication);

        // Assert
        assertFalse(id > 0);

        // Clean
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
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
        int adopterId = this.createAdopter("john.Doe4@example.com");
        int petId = this.createPet();
        ApplicationStatus status = ApplicationStatus.REJECTED;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";
        String closingDate = "2001-12-15";

        AdoptionApplication adoptionApplication = new AdoptionApplication(adopterId, petId, status, description, experience, creationDate, closingDate);


        // Add the record to the DB
        int id = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(id > 0);

        adoptionApplication.setId(id);

        // Act
        boolean isSuccess = adoptionApplicationDAO.delete(adoptionApplication);

        // Assert
        assertTrue(isSuccess);

        // Clean
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
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
        int adopterId = this.createAdopter("john.Doe5@example.com");
        int petId = this.createPet();
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
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
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
        int adopterId = this.createAdopter("john.Doe6@example.com");
        int petId = this.createPet();
        ApplicationStatus status = ApplicationStatus.REJECTED;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";
        String closingDate = "2001-12-15";

        AdoptionApplication adoptionApplication = new AdoptionApplication(adopterId, petId, status, description, experience, creationDate, closingDate);


        // Add the record to the DB
        int id = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(id > 0);

        // Act
        List<AdoptionApplication> fetchedAdoptionApps = adoptionApplicationDAO.findByAdopterId(adopterId);

        // Assert
        assertTrue(fetchedAdoptionApps.contains(adoptionApplication));

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
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
        int adopterId = this.createAdopter("john.Doe7@example.com");
        int petId = this.createPet();
        ApplicationStatus status = ApplicationStatus.REJECTED;
        String description = "I need to adopt this pet.";
        Boolean experience = false;
        String creationDate = "1990-12-13";
        String closingDate = "2001-12-15";

        AdoptionApplication adoptionApplication = new AdoptionApplication(adopterId, petId, status, description, experience, creationDate, closingDate);


        // Add the record to the DB
        int id = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(id > 0);

        adoptionApplication.setId(id);

        // Act
        List<AdoptionApplication> fetchedAdoptionApps = adoptionApplicationDAO.findByPetId(petId);

        // Assert
        assertTrue(fetchedAdoptionApps.contains(adoptionApplication));

        // clean
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", id);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    public void testAdoptionApplicationFindStatusValid() {
        // Arrange
        int adopterId1 = this.createAdopter("john.Doe8@example.com");
        int petId1 = this.createPet();
        ApplicationStatus status1 = ApplicationStatus.REJECTED;
        String description1 = "I need to adopt this pet.";
        Boolean experience1 = false;
        String creationDate1 = "1990-12-13";
        String closingDate1 = "2001-12-15";

        AdoptionApplication adoptionApplication1 = new AdoptionApplication(adopterId1, petId1, status1, description1, experience1, creationDate1, closingDate1);

        int adopterId2 = this.createAdopter("john.Doe9@example.com");
        int petId2 = this.createPet();
        ApplicationStatus status2 = ApplicationStatus.APPROVED;
        String description2 = "I love this pet.";
        Boolean experience2 = true;
        String creationDate2 = "1995-11-16";
        String closingDate2 = "2020-01-12";

        AdoptionApplication adoptionApplication2 = new AdoptionApplication(adopterId2, petId2, status2, description2, experience2, creationDate2, closingDate2);


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
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId2);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
    }

    @Test
    public void testAdoptionApplicationFindAll() {
        // Arrange
        int adopterId1 = this.createAdopter("john.Doe10@example.com");
        int petId1 = this.createPet();
        ApplicationStatus status1 = ApplicationStatus.REJECTED;
        String description1 = "I need to adopt this pet.";
        Boolean experience1 = false;
        String creationDate1 = "1990-12-13";
        String closingDate1 = "2001-12-15";

        AdoptionApplication adoptionApplication1 = new AdoptionApplication(adopterId1, petId1, status1, description1, experience1, creationDate1, closingDate1);

        int adopterId2 = this.createAdopter("john.Doe11@example.com");
        int petId2 = this.createPet();
        ApplicationStatus status2 = ApplicationStatus.APPROVED;
        String description2 = "I love this pet.";
        Boolean experience2 = true;
        String creationDate2 = "1995-11-16";
        String closingDate2 = "2020-01-12";

        AdoptionApplication adoptionApplication2 = new AdoptionApplication(adopterId2, petId2, status2, description2, experience2, creationDate2, closingDate2);

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
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId2);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
    }

}
