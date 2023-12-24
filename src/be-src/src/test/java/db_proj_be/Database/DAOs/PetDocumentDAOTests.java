package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.PetDocument;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class PetDocumentDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PetDocumentDAO petDocumentDAO;

    @BeforeAll
    public void setUp() {
        petDocumentDAO = new PetDocumentDAO(jdbcTemplate);
    }

    @Test
    @DisplayName ("PetDocumentDAO - save document successfully")
    public void saveDocumentSuccessfully() {
        // Arrange
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
        jdbcTemplate.update("INSERT INTO PET (id, name, specie, breed, gender, health_status) VALUES (?, ?, ?, ?, ?, ?)",
                100, "Dog", "test", "test", true, "test");

        // Act
        PetDocument petDocument = new PetDocument(100, "image", new byte[]{1, 2, 3});
        boolean result = petDocumentDAO.saveDocument(petDocument);

        // Assert
        assertTrue(result);

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
    }

    @Test
    @DisplayName ("PetDocumentDAO - save document failed by passing null document")
    public void saveDocumentFailed() {
        // Act
        boolean result = petDocumentDAO.saveDocument(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName ("PetDocumentDAO - save document failed by violating a constraint")
    public void saveDocumentFailedByConstraint() {
        // Arrange
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);

        // Act
        boolean result = petDocumentDAO.saveDocument(new PetDocument(100, "image", new byte[]{1, 2, 3}));

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents of pet given its id")
    public void findDocumentsById() {
        // Arrange
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
        jdbcTemplate.update("INSERT INTO PET (id, name, specie, breed, gender, health_status) VALUES (?, ?, ?, ?, ?, ?)",
                100, "Dog", "test", "test", true, "test");
        jdbcTemplate.update("INSERT INTO PET_DOCUMENT (pet_id, document_type, document) VALUES (?, ?, ?)",
                100, "image", new byte[]{1, 2, 3});

        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetId(100);

        // Assert
        assertNotNull(documents);
        assertEquals(100, documents.get(0).getPetId());
        assertEquals("image", documents.get(0).getDocumentType());
        assertArrayEquals(new byte[]{1, 2, 3}, documents.get(0).getDocument());

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents failed due to passing invalid id")
    public void findDocumentsByIdFailed() {
        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetId(-1);

        // Assert
        assertNotNull(documents);
        assertEquals(0, documents.size());
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents of pet given its id and document type")
    public void findDocumentsByIdAndType() {
        // Arrange
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
        jdbcTemplate.update("INSERT INTO PET (id, name, specie, breed, gender, health_status) VALUES (?, ?, ?, ?, ?, ?)",
                100, "Dog", "test", "test", true, "test");
        jdbcTemplate.update("INSERT INTO PET_DOCUMENT (pet_id, document_type, document) VALUES (?, ?, ?)",
                100, "image", new byte[]{1, 2, 3});

        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetIdAndType(100, "image");

        // Assert
        assertNotNull(documents);
        assertEquals(100, documents.get(0).getPetId());
        assertEquals("image", documents.get(0).getDocumentType());
        assertArrayEquals(new byte[]{1, 2, 3}, documents.get(0).getDocument());

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
    }

    @Test
    @DisplayName("PetDocumentDAO - find all documents failed due to passing invalid id")
    public void findDocumentsByIdAndTypeFailed() {
        // Act
        List<PetDocument> documents = petDocumentDAO.findByPetIdAndType(-1, null);

        // Assert
        assertNotNull(documents);
        assertEquals(0, documents.size());
    }

    @Test
    @DisplayName ("PetDocumentDAO - delete document successfully")
    public void deleteDocumentSuccessfully() {
        // Arrange
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
        jdbcTemplate.update("INSERT INTO PET (id, name, specie, breed, gender, health_status) VALUES (?, ?, ?, ?, ?, ?)",
                100, "Dog", "test", "test", true, "test");
        jdbcTemplate.update("INSERT INTO PET_DOCUMENT (pet_id, document_type, document) VALUES (?, ?, ?)", 100, "image", new byte[]{1, 2, -3});

        // Act
        boolean result = petDocumentDAO.deleteDocument(new PetDocument(100, "image", new byte[]{1, 2, -3}));

        // Assert
        assertTrue(result);

        // Clean
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
    }

    @Test
    @DisplayName ("PetDocumentDAO - delete document failed by passing null document")
    public void deleteDocumentFailed() {
        // Act
        boolean result = petDocumentDAO.deleteDocument(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName ("PetDocumentDAO - delete document not exist")
    public void deleteDocumentNotExist() {
        // Arrange
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);

        // Act
        boolean result = petDocumentDAO.deleteDocument(new PetDocument(100, "image", new byte[]{1, 2, -3}));

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName ("PetDocumentDAO - update document successfully")
    public void updateDocumentSuccessfully() {
        // Arrange
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
        jdbcTemplate.update("INSERT INTO PET (id, name, specie, breed, gender, health_status) VALUES (?, ?, ?, ?, ?, ?)",
                100, "Dog", "test", "test", true, "test");
        jdbcTemplate.update("INSERT INTO PET_DOCUMENT (pet_id, document_type, document) VALUES (?, ?, ?)",
                100, "image", new byte[]{-1, -2, -3});
        PetDocument document = jdbcTemplate.query("SELECT * FROM PET_DOCUMENT WHERE pet_id = ?", petDocumentDAO.getRowMapper(), 100).get(0);

        // Act
        document.setDocument(new byte[]{1, 2, 3});
        document.setDocumentType("photo");
        boolean result = petDocumentDAO.updateDocument(document);

        // Assert
        assertTrue(result);

        // Clean
        jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", 100);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);
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
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", 100);

        // Act
        PetDocument document = new PetDocument(100, "image", new byte[]{1, 2, 3});
        document.setId(-1);
        boolean result = petDocumentDAO.updateDocument(document);

        // Assert
        assertFalse(result);
    }
}
