package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.EntityModels.PetDocument;
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
public class PetDocumentDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private PetDocumentDAO petDocumentDAO;
    private PetDAO petDAO;

    @BeforeAll
    public void setUp() {
        petDocumentDAO = new PetDocumentDAO(jdbcTemplate);
        petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName ("PetDocumentDAO - create document successfully")
    public void createDocumentSuccessfully() {
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
        int documentId = petDocumentDAO.create(new PetDocument(petId, "image", "test.jpeg", new byte[]{1, 2, 3}));
        assertTrue(documentId >= 1);

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE id = ?", documentId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName ("PetDocumentDAO - create document failed by passing null document")
    public void saveDocumentFailed() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> petDocumentDAO.create(null));
    }

    @Test
    @DisplayName ("PetDocumentDAO - save document failed by violating a constraint")
    public void saveDocumentFailedByConstraint() {
        // Arrange
        // Act
        int documentId = petDocumentDAO.create(new PetDocument(-1, "image", "test.jpeg", new byte[]{1, 2, 3}));

        // Assert
        assertEquals(-1, documentId);
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents of pet given its pet id")
    public void findDocumentsByPetId() {
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

        int documentId = petDocumentDAO.create(new PetDocument(petId, "image", "test.jpeg", new byte[]{1, 2, 3}));
        assertTrue(documentId >= 1);

        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetId(petId);

        // Assert
        assertNotNull(documents);
        assertEquals(petId, documents.get(0).getPetId());
        assertEquals("image", documents.get(0).getDocumentType());
        assertArrayEquals(new byte[]{1, 2, 3}, documents.get(0).getDocument());

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE id = ?", documentId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents failed due to passing invalid pet id")
    public void findDocumentsByPetIdFailed() {
        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetId(-1);

        // Assert
        assertNotNull(documents);
        assertEquals(0, documents.size());
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents of pet given its pet id and document type")
    public void findDocumentsByPetIdAndType() {
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

        int documentId = petDocumentDAO.create(new PetDocument(petId, "image", "test.jpeg", new byte[]{1, 2, 3}));
        assertTrue(documentId >= 1);

        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetIdAndType(petId, "image");

        // Assert
        assertNotNull(documents);
        assertEquals(petId, documents.get(0).getPetId());
        assertEquals("image", documents.get(0).getDocumentType());
        assertArrayEquals(new byte[]{1, 2, 3}, documents.get(0).getDocument());

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE id = ?", documentId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents failed due to passing invalid pet id and type")
    public void findDocumentsByIdAndTypeFailed() {
        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetIdAndType(-1, null);

        // Assert
        assertNotNull(documents);
        assertEquals(0, documents.size());
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents of pet given its pet id and document name")
    public void findDocumentsByPetIdAndName() {
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

        int documentId = petDocumentDAO.create(new PetDocument(petId, "image", "test.jpeg", new byte[]{1, 2, 3}));
        assertTrue(documentId >= 1);

        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetIdAndName(petId, "test.jpeg");

        // Assert
        assertNotNull(documents);
        assertEquals(petId, documents.get(0).getPetId());
        assertEquals("test.jpeg", documents.get(0).getName());
        assertArrayEquals(new byte[]{1, 2, 3}, documents.get(0).getDocument());

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE id = ?", documentId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents failed due to passing invalid pet id and name")
    public void findDocumentsByIdAndNameFailed() {
        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetIdAndName(-1, null);

        // Assert
        assertNotNull(documents);
        assertEquals(0, documents.size());
    }
    @Test
    @DisplayName ("PetDocumentDAO - delete document successfully")
    public void deleteDocumentSuccessfully() {
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

        PetDocument document = new PetDocument(petId, "image", "test.jpeg", new byte[]{1, 2, 3});
        int documentId = petDocumentDAO.create(document);
        document.setId(documentId);
        assertTrue(documentId >= 1);

        // Act
        boolean result = petDocumentDAO.delete(document);

        // Assert
        assertTrue(result);

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName ("PetDocumentDAO - delete document failed by passing null document")
    public void deleteDocumentFailed() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> petDocumentDAO.delete(null));

    }

    @Test
    @DisplayName ("PetDocumentDAO - update document successfully")
    public void updateDocumentSuccessfully() {
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

        PetDocument document = new PetDocument(petId, "image", "test.jpeg", new byte[]{-1, -2, -3});
        int documentId = petDocumentDAO.create(document);
        assertTrue(documentId >= 1);
        // Act
        document.setDocument(new byte[]{1, 2, 3});
        document.setDocumentType("photo");
        document.setId(documentId);
        boolean result = petDocumentDAO.updateDocument(document);

        // Assert
        assertTrue(result);

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE id = ?", documentId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId);
    }

    @Test
    @DisplayName ("PetDocumentDAO - update document failed by passing null document")
    public void updateDocumentFailed() {
        // Act
        boolean result = petDocumentDAO.updateDocument(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName ("PetDocumentDAO - update document failed by document not exists")
    public void updateDocumentFailedByConstraint() {
        // Arrange

        // Act
        PetDocument document = new PetDocument(-1, "image", "test", new byte[]{1, 2, 3});
        boolean result = petDocumentDAO.updateDocument(document);

        // Assert
        assertFalse(result);
    }
}
