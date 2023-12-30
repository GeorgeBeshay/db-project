package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class PetDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private PetDAO petDAO;

    @BeforeAll
    public void setUp() {
        petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName ("PetDAO - create pet successfully")
    public void createPetSuccessfully() {
        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setBirthdate("2018-5-6");
        pet.setHealthStatus("test");

        // Act
        int petId = petDAO.create(pet);
        assertTrue(petId >= 1);

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName ("PetDAO - create pet failed by passing null object")
    public void createPetFailed() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> petDAO.create(null));
    }

    @Test
    @DisplayName ("PetDAO - create pet failed by violating constraints")
    public void saveDocumentFailedByConstraint() {
        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");

        // Act
        int petId = petDAO.create(pet);

        // Assert
        assertEquals(-1, petId);
    }

    @Test
    @DisplayName ("PetDAO - update pet successfully")
    public void updatePetSuccessfully() {
        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setBirthdate("2018-5-6");
        pet.setHealthStatus("test");
        int petId = petDAO.create(pet);
        assertTrue(petId >= 1);

        // Act
        pet.setId(petId);
        pet.setGender(false);
        pet.setBirthdate("2017-01-01");
        boolean result = petDAO.update(pet);

        Pet updatedPet = petDAO.findById(petId);
        assertNotNull(updatedPet);

        // Assert
        assertTrue(result);
        assertEquals(petId, updatedPet.getId());
        assertFalse(updatedPet.isGender());
        assertEquals("2017-01-01", updatedPet.getBirthdate());

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName ("PetDAO - update pet failed by passing null document")
    public void updatePetFailed() {
        // Act
        boolean result = petDAO.update(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName ("PetDAO - update pet failed by pet not exists")
    public void updatePetFailedByConstraint() {
        // Arrange
        Pet pet = new Pet();
        pet.setId(-1);
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setBirthdate("2018-5-6");
        pet.setHealthStatus("test");

        // Act
        boolean result = petDAO.update(pet);

        // Assert
        assertFalse(result);
    }


    @Test
    @DisplayName ("PetDAO - sort pets successfully")
    public void sortPetsSuccessfully() {
        // Arrange
        Pet pet1 = new Pet();
        pet1.setName("Dog");
        pet1.setSpecie("test");
        pet1.setBreed("test");
        pet1.setGender(true);
        pet1.setBirthdate("1001-5-6");
        pet1.setHealthStatus("test");

        Pet pet2 = new Pet();
        pet2.setName("Dog");
        pet2.setSpecie("test");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setBirthdate("1000-5-6");
        pet2.setHealthStatus("test");

        // Act
        int petId1 = petDAO.create(pet1);
        int petId2 = petDAO.create(pet2);

        List<Pet> sortedPets = petDAO.sortByAttribute("birthdate", true);

        // Assert
        assertTrue(petId1 >= 1);
        assertTrue(petId2 >= 1);
        assertEquals(petId2, sortedPets.get(0).getId());
        assertEquals(petId1, sortedPets.get(1).getId());
        assertEquals("1000-05-06", sortedPets.get(0).getBirthdate());
        assertEquals("1001-05-06", sortedPets.get(1).getBirthdate());

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
    }

    @Test
    @DisplayName ("PetDAO - sort pets descending successfully")
    public void sortPetsSuccessfullyDescending() {
        // Arrange
        Pet pet1 = new Pet();
        pet1.setName("Dog");
        pet1.setSpecie("test");
        pet1.setBreed("test");
        pet1.setGender(true);
        pet1.setBirthdate("3001-5-6");
        pet1.setHealthStatus("test");

        Pet pet2 = new Pet();
        pet2.setName("Dog");
        pet2.setSpecie("test");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setBirthdate("3000-5-6");
        pet2.setHealthStatus("test");

        // Act
        int petId1 = petDAO.create(pet1);
        int petId2 = petDAO.create(pet2);

        List<Pet> sortedPets = petDAO.sortByAttribute("birthdate", false);

        // Assert
        assertTrue(petId1 >= 1);
        assertTrue(petId2 >= 1);
        assertEquals(petId1, sortedPets.get(0).getId());
        assertEquals(petId2, sortedPets.get(1).getId());
        assertEquals("3001-05-06", sortedPets.get(0).getBirthdate());
        assertEquals("3000-05-06", sortedPets.get(1).getBirthdate());

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
    }

    @Test
    @DisplayName ("PetDAO - sort pet failed by passing invalid attribute")
    public void sortPetsFailed() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> petDAO.sortByAttribute("test", true));
    }

    @Test
    @DisplayName ("PetDAO - filter pets successfully with specie")
    public void filterPetsSuccessfullyBySpecie() {
        // Arrange
        Pet pet1 = new Pet();
        pet1.setName("test");
        pet1.setSpecie("dog");
        pet1.setBreed("test");
        pet1.setGender(true);
        pet1.setHealthStatus("test");

        Pet pet2 = new Pet();
        pet2.setName("test");
        pet2.setSpecie("cat");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setHealthStatus("test");

        // Act
        int petId1 = petDAO.create(pet1);
        int petId2 = petDAO.create(pet2);

        Map<String, Object> attributesToValue = new HashMap<>();
        attributesToValue.put("specie", "cat");
        List<Pet> cats = petDAO.findByAttributes(attributesToValue);

        // Assert
        assertTrue(petId1 >= 1);
        assertTrue(petId2 >= 1);
        assertTrue(cats.size() > 0);
        assertEquals("cat", cats.get(0).getSpecie());

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
    }

    @Test
    @DisplayName ("PetDAO - filter pets successfully with specie and gender")
    public void filterPetsSuccessfullyBySpecieAndGender() {
        // Arrange
        Pet pet1 = new Pet();
        pet1.setName("test");
        pet1.setSpecie("dog");
        pet1.setBreed("test");
        pet1.setGender(true);
        pet1.setHealthStatus("test");

        Pet pet2 = new Pet();
        pet2.setName("test");
        pet2.setSpecie("cat");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setHealthStatus("test");

        // Act
        int petId1 = petDAO.create(pet1);
        int petId2 = petDAO.create(pet2);

        Map<String, Object> attributesToValue = new HashMap<>();
        attributesToValue.put("specie", "cat");
        attributesToValue.put("gender", true);
        List<Pet> catsMan = petDAO.findByAttributes(attributesToValue);

        // Assert
        assertTrue(petId1 >= 1);
        assertTrue(petId2 >= 1);
        assertTrue(catsMan.size() > 0);
        assertEquals("cat", catsMan.get(0).getSpecie());
        assertTrue(catsMan.get(0).isGender());

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
    }

    @Test
    @DisplayName ("PetDAO - filter pets successfully with empty map")
    public void filterPetsSuccessfullyAll() {
        // Arrange
        Pet pet1 = new Pet();
        pet1.setName("test");
        pet1.setSpecie("dog");
        pet1.setBreed("test");
        pet1.setGender(true);
        pet1.setHealthStatus("test");

        Pet pet2 = new Pet();
        pet2.setName("test");
        pet2.setSpecie("cat");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setHealthStatus("test");

        // Act
        int petId1 = petDAO.create(pet1);
        int petId2 = petDAO.create(pet2);

        Map<String, Object> attributesToValue = new HashMap<>();
        List<Pet> pets = petDAO.findByAttributes(attributesToValue);

        // Assert
        assertTrue(petId1 >= 1);
        assertTrue(petId2 >= 1);
        assertTrue(pets.size() > 0);

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId1);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2);
    }

    @Test
    @DisplayName ("PetDAO - filter pets failed by passing null map")
    public void filterPetsFailed() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> petDAO.findByAttributes(null));

    }

    @Test
    @DisplayName("Pet DAO Tests - Deletion: Passing a null object.")
    public void testPetDeletionNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> petDAO.delete(null));
    }

    @Test
    @DisplayName("Pet DAO Tests - Deletion: Passing an object that doesn't exist in the current DB state.")
    public void testPetDeletionObjectDoesntExist() {
        // Arrange - none
        Pet pet = new Pet();

        // Act
        boolean isSuccess = petDAO.delete(pet);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Pet DAO Tests - Deletion: Passing a valid object")
    public void testPetDeletionValidObject() {
        // Arrange
        int petId = -1;

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        // Add the record to the DB
        petId = petDAO.create(pet);
        assertTrue(petId >= 1);
        pet.setId(petId);

        // Act
        boolean isSuccess = petDAO.delete(pet);

        // Assert
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Pet DAO Tests - Find: Finding all pet records.")
    public void testPetFindAll() {
        // Arrange
        int petId = -1, petId2 = -1;

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        Pet pet2 = new Pet();
        pet2.setName("Dog");
        pet2.setSpecie("test");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setHealthStatus("test");

        // Add the record to the DB
        petId = petDAO.create(pet);
        assertTrue(petId >= 1);
        pet.setId(petId);

        petId2 = petDAO.create(pet2);
        assertTrue(petId2 >= 1);
        pet2.setId(petId2);

        // Act
        List<Pet> fetchedPets = petDAO.findAll();

        // Assert
        assertTrue(fetchedPets.size() >= 2);
        assertTrue(fetchedPets.contains(pet));
        assertTrue(fetchedPets.contains(pet2));

        // Clean
        assertTrue(petDAO.delete(pet));
        assertTrue(petDAO.delete(pet2));

    }

//    @Test
//    public void test() {
//        List<Integer> shelterIds = Arrays.asList(1, 2);
//        Map<String, Object> criteria = new HashMap<>();
//        criteria.put("specie", "dog");
//        criteria.put("gender", true);
//        List<String> columns = Arrays.asList("neutering", "house_training");
//
//        List<Pet> pets = petDAO.getPetsWithOptionsAndSorted(shelterIds, criteria, columns);
//
//        System.out.println(pets);
//
//    }

}
