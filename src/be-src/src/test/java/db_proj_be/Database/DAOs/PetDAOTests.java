package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class PetDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private PetDAO petDAO;
    private ShelterDAO shelterDAO;

    @BeforeAll
    public void setUp() {
        petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        shelterDAO = new ShelterDAO(jdbcTemplate);
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
    @DisplayName ("PetDAO - filter pets failed with exception")
    public void filterPetsFailedWithException() {
        // Act
        Map<String, Object> attributesToValue = new HashMap<>();
        attributesToValue.put("No column", true);
        List<Pet> pets = petDAO.findByAttributes(attributesToValue);

        // Assert
        assertNull(pets);
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

    @Test
    @DisplayName("Pet DAO Tests - Get unAdopted Pets")
    public void testGetUnAdoptedPetsSuccessfully() {
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
        List<Pet> unAdoptedPets = petDAO.getUnAdoptedPets();

        // Assert
        assertTrue(unAdoptedPets.size() >= 1);
        assertTrue(unAdoptedPets.contains(pet));

        // Clean
        assertTrue(petDAO.delete(pet));

    }

    @Test
    @DisplayName("Pet DAO Tests - Get unAdopted Pets failed with exception")
    public void testGetUnAdoptedPetsFailedWithException() {
        // Arrange
        JdbcTemplate mockJdbcTemplate = mock(JdbcTemplate.class);
        PetDAO newPetDAO = new PetDAO(mockJdbcTemplate, namedParameterJdbcTemplate);

        when(mockJdbcTemplate.query(
                anyString(),
                any(newPetDAO.getRowMapper().getClass()))
        )
                .thenThrow(new DataAccessException("Simulated database error") {});
        // Act
        List<Pet> unAdoptedPets = newPetDAO.getUnAdoptedPets();

        // Assert
        assertNull(unAdoptedPets);
    }

    @Test
    @DisplayName("Pet DAO Tests - Get pet with options and sorted")
    public void testGetPetsWithOptionsAndSorted() {
        // Arrange
        int shelterId = -1, petId = -1, petId2 = -1;

        Shelter shelter = new Shelter();
        shelter.setName("shelter");
        shelter.setEmail("shleter@gmail.com");
        shelter.setPhone("0152");
        shelter.setLocation("alex");

        shelterId = shelterDAO.create(shelter);
        shelter.setId(shelterId);

        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");
        pet.setNeutering(false);
        pet.setShelterId(shelterId);

        Pet pet2 = new Pet();
        pet2.setName("Dog");
        pet2.setSpecie("test");
        pet2.setBreed("test");
        pet2.setGender(true);
        pet2.setHealthStatus("test");
        pet2.setShelterId(shelterId);

        petId = petDAO.create(pet);
        pet.setId(petId);

        petId2 = petDAO.create(pet2);
        pet2.setId(petId2);

        // Act
        List<Integer> shelterIds = new ArrayList<>(List.of(shelterId));
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("specie", "test");
        criteria.put("breed", "test");
        List<String> orderBy = new ArrayList<>(List.of("neutering"));

        List<Pet> fetchedPets = petDAO.getPetsWithOptionsAndSorted(shelterIds, criteria, orderBy);

        // Assert
        assertTrue(petId >= 1);
        assertTrue(petId2 >= 1);
        assertTrue(shelterId >= 1);
        assertEquals(2, fetchedPets.size());
        assertTrue(fetchedPets.contains(pet));
        assertTrue(fetchedPets.contains(pet2));

        // Clean
        assertTrue(petDAO.delete(pet));
        assertTrue(petDAO.delete(pet2));
        assertTrue(shelterDAO.delete(shelter));

    }

    @Test
    @DisplayName("Pet DAO Tests - Get pet with options and sorted with Exception")
    public void testGetPetsWithOptionsAndSortedException() {
        // Act
        List<String> orderBy = new ArrayList<>(List.of("no column"));

        List<Pet> fetchedPets = petDAO.getPetsWithOptionsAndSorted(null, null, orderBy);

        // Assert
        assertNull(fetchedPets);

    }

}
