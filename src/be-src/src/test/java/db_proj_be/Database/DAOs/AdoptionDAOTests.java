package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Adopter;
import db_proj_be.BusinessLogic.EntityModels.Adoption;
import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class AdoptionDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private AdoptionDAO adoptionDAO;

    @BeforeEach
    public void initAdoptionDAO() {
        // enforce independence between the tests.
        this.adoptionDAO = new AdoptionDAO(jdbcTemplate);

        // Assert that the testing IDs are valid.
        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        assert adopterDAO.findById(-1) == null : "Adopter Testing ID Shouldn't exist in the current DB State.";

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        assert petDAO.findById(-1) == null : "Pet Testing ID Shouldn't exist in the current DB State.";
    }

    @AfterAll
    public void finishTests() {
        Logger.logMsgFrom(this.getClass().getName(), "AdopterDAOTests finished.", -1);
    }

    // ------------------------- Creation Tests -------------------------

    @Test
    @DisplayName("Adoption DAO Tests - Creation: Passing a null object.")
    public void testAdoptionCreationNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adoptionDAO.create(null));
    }

    @Test
    @DisplayName("Adoption DAO Tests - Creation: Passing an object that contains invalid adopterId and petId.")
    public void testAdoptionCreationInvalidObject01() {
        // Arrange
        int adopterId = -1;     // both shouldn't exist in the current DB state.
        int petId = -1;
        Adoption adoption = new Adoption(petId, adopterId);

        // Act
        boolean isSuccess = adoptionDAO.create(adoption);

        // Assert
        assertFalse(isSuccess);

    }

    @Test
    @DisplayName("Adoption DAO Tests - Creation: Passing an object that contains invalid adopterId and valid petId.")
    public void testAdoptionCreationInvalidObject02() {
        // Arrange
        int adopterId = -1;     // shouldn't exist in the current DB state.
        int petId = -1;

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId = petDAO.create(pet);
        assertTrue(petId >= 1);

        Adoption adoption = new Adoption(petId, adopterId);

        // Act
        boolean isSuccess = adoptionDAO.create(adoption);

        // Assert
        assertFalse(isSuccess);

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName("Adoption DAO Tests - Creation: Passing an object that contains valid adopterId and invalid petId.")
    public void testAdoptionCreationInvalidObject03() {
        // Arrange
        int petId = -1;     // shouldn't exist in the DB.
        int adopterId;

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

        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        adopterId = adopterDAO.create(adopter);
        assertTrue(adopterId >= 1);

        Adoption adoption = new Adoption(petId, adopterId);

        // Act
        boolean isSuccess = adoptionDAO.create(adoption);

        // Assert
        assertFalse(isSuccess);

        // Clean
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
    }

    @Test
    @DisplayName("Adoption DAO Tests - Creation: Passing an object that contains valid adopterId and valid petId.")
    public void testAdoptionCreationValidObject() {
        // Arrange
        int petId = -1;     // shouldn't exist in the DB.
        int adopterId = -1;

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

        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        adopterId = adopterDAO.create(adopter);
        assertTrue(adopterId >= 1);

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId = petDAO.create(pet);
        assertTrue(petId >= 1);


        Adoption adoption = new Adoption(petId, adopterId);

        // Act
        boolean isSuccess = adoptionDAO.create(adoption);

        // Assert
        assertTrue(isSuccess);

        // Clean
        assertTrue(adoptionDAO.delete(adoption));
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    // ------------------------- Deletion Tests -------------------------

    @Test
    @DisplayName("Adoption DAO Tests - Deletion: Passing a null object.")
    public void testAdoptionDeletionNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adoptionDAO.delete(null));
    }

    @Test
    @DisplayName("Adoption DAO Tests - Deletion: Passing an object that contains valid adopterId and invalid petId")
    public void testAdoptionDeletionInvalidObject01() {
        // Arrange - none
        int petId = -1;
        int adopterId = -1;

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

        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        adopterId = adopterDAO.create(adopter);
        assertTrue(adopterId >= 1);

        Adoption adoption = new Adoption(petId, adopterId);

        // Act
        boolean isSuccess = adoptionDAO.delete(adoption);

        // Assert
        assertFalse(isSuccess);

        // Clean
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId);
    }

    @Test
    @DisplayName("Adoption DAO Tests - Deletion: Passing an object that contains invalid adopterId and valid petId")
    public void testAdoptionDeletionInvalidObject02() {
        // Arrange - none
        int petId = -1;
        int adopterId = -1;

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId = petDAO.create(pet);
        assertTrue(petId >= 1);

        Adoption adoption = new Adoption(petId, adopterId);

        // Act
        boolean isSuccess = adoptionDAO.delete(adoption);

        // Assert
        assertFalse(isSuccess);

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    // ------------------------- Find Tests -------------------------

    @Test
    @DisplayName("Adoption DAO Tests - Find: Finding all adoption records.")
    public void testAdoptionFindAll() {
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
        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);

        int adopterId1 = adopterDAO.create(adopter);
        assertTrue(adopterId1 >= 1);
        adopter.setId(adopterId1);

        int adopterId2 = adopterDAO.create(adopter2);
        assertTrue(adopterId2 >= 1);
        adopter2.setId(adopterId2);

        int petId1 = -1;
        int petId2 = -1;

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId1 = petDAO.create(pet);
        assertTrue(petId1 >= 1);

        Pet pet2 = new Pet();
        pet2.setName("Dog");
        pet2.setSpecie("test");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setHealthStatus("test");

        petId2 = petDAO.create(pet2);
        assertTrue(petId2 >= 1);

        Adoption adoption1 = new Adoption(petId1, adopterId1);
        Adoption adoption2 = new Adoption(petId2, adopterId2);

        assertTrue(adoptionDAO.create(adoption1));
        assertTrue(adoptionDAO.create(adoption2));

        // Act
        List<Adoption> fetchedAdoption = adoptionDAO.findAll();

        // Assert
        assertTrue(fetchedAdoption.size() >= 2);
        assertTrue(fetchedAdoption.contains(adoption1));
        assertTrue(fetchedAdoption.contains(adoption2));

        // Clean
        assertTrue(adoptionDAO.delete(adoption1));
        assertTrue(adoptionDAO.delete(adoption2));
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId1);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId2);

    }

    @Test
    @DisplayName("Adoption DAO Tests - Find: Finding adoption records by pet id.")
    public void testAdoptionFindByPetId() {
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
        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);

        int adopterId1 = adopterDAO.create(adopter);
        assertTrue(adopterId1 >= 1);
        adopter.setId(adopterId1);

        int petId1 = -1;
        int petId2 = -1;

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId1 = petDAO.create(pet);
        assertTrue(petId1 >= 1);

        Pet pet2 = new Pet();
        pet2.setName("Dog");
        pet2.setSpecie("test");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setHealthStatus("test");

        petId2 = petDAO.create(pet2);
        assertTrue(petId2 >= 1);

        Adoption expectedAdoption1 = new Adoption(petId1, adopterId1);
        Adoption expectedAdoption2 = new Adoption(petId2, adopterId1);

        assertTrue(adoptionDAO.create(expectedAdoption1));
        assertTrue(adoptionDAO.create(expectedAdoption2));

        // Act
        Adoption actualAdoption1 = adoptionDAO.findByPetId(petId1);
        Adoption actualAdoption2 = adoptionDAO.findByPetId(petId2);

        // Assert
        assertEquals(expectedAdoption1, actualAdoption1);
        assertEquals(expectedAdoption2, actualAdoption2);

        // Clean
        assertTrue(adoptionDAO.delete(expectedAdoption1));
        assertTrue(adoptionDAO.delete(expectedAdoption2));
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId1);

    }

    @Test
    @DisplayName("Adoption DAO Tests - Find: Finding adoption records by adopter id.")
    public void testAdoptionFindByAdopterId() {
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
        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);

        int adopterId1 = adopterDAO.create(adopter);
        assertTrue(adopterId1 >= 1);
        adopter.setId(adopterId1);

        int petId1 = -1;
        int petId2 = -1;

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId1 = petDAO.create(pet);
        assertTrue(petId1 >= 1);

        Pet pet2 = new Pet();
        pet2.setName("Dog");
        pet2.setSpecie("test");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setHealthStatus("test");

        petId2 = petDAO.create(pet2);
        assertTrue(petId2 >= 1);

        Adoption expectedAdoption1 = new Adoption(petId1, adopterId1);
        Adoption expectedAdoption2 = new Adoption(petId2, adopterId1);

        assertTrue(adoptionDAO.create(expectedAdoption1));
        assertTrue(adoptionDAO.create(expectedAdoption2));

        // Act
        List<Adoption> fetchedAdoptions = adoptionDAO.findByAdopterId(adopterId1);

        // Assert
        assertEquals(2, fetchedAdoptions.size());
        assertTrue(fetchedAdoptions.contains(expectedAdoption1));
        assertTrue(fetchedAdoptions.contains(expectedAdoption2));

        // Clean
        assertTrue(adoptionDAO.delete(expectedAdoption1));
        assertTrue(adoptionDAO.delete(expectedAdoption2));
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId1);

    }


}
