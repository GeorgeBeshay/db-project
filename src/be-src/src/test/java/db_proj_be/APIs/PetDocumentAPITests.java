package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.EntityModels.PetDocument;
import db_proj_be.Database.DAOs.PetDAO;
import db_proj_be.Database.DAOs.PetDocumentDAO;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
@AutoConfigureMockMvc
public class PetDocumentAPITests {

    private final MockMvc mockMvc;
    private final JdbcTemplate jdbcTemplate;
    private final PetDAO petDAO;
    private final PetDocumentDAO petDocumentDAO;

    @Autowired
    public PetDocumentAPITests(MockMvc mockMvc, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
        this.petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        this.petDocumentDAO = new PetDocumentDAO(jdbcTemplate);
    }

    @Test
    @DisplayName("Create documents for pet")
    public void testCreateDocumentsForPet() throws Exception {
        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        int petId = this.petDAO.create(pet);
        pet.setId(petId);

        // Act
        MockMultipartFile petMultipartFile = new MockMultipartFile("files", "Test Document", "text/plain", new byte[] {1, 2, 3});

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("http://localhost:8081/pasms-server/pet-document-api/upload/" + petId)
                        .file(petMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn();

        int status = result.getResponse().getStatus();
        int resultValue = Integer.parseInt(result.getResponse().getContentAsString());

        // Assert
        assertTrue(petId > 0);
        assertEquals(HttpStatus.OK.value(), status);
        assertEquals(1, resultValue);

        // Clean
        this.jdbcTemplate.update("DELETE FROM PET_DOCUMENT WHERE pet_id = ?", petId);
        this.petDAO.delete(pet);
    }

    @Test
    @DisplayName("Create documents for pet Failed")
    public void testCreateDocumentsForPetFailed() throws Exception {
        // Act
        MockMultipartFile petMultipartFile = new MockMultipartFile("files", "Test Document", "text/plain", new byte[] {1, 2, 3});

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("http://localhost:8081/pasms-server/pet-document-api/upload/" + -1)
                        .file(petMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn();

        int status = result.getResponse().getStatus();
        int resultValue = Integer.parseInt(result.getResponse().getContentAsString());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals(0, resultValue);
    }

    @Test
    @DisplayName("Download Document")
    public void testDownloadDocument() throws Exception {
        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");
        int petId = petDAO.create(pet);

        PetDocument document = new PetDocument(petId, "text/plain", "test.txt", new byte[]{1, 2, 3});
        int documentId = petDocumentDAO.create(document);
        document.setId(documentId);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8081/pasms-server/pet-document-api/download/" + documentId))
                .andReturn();

        // Assert
        assertTrue(petId > 0);
        assertTrue(documentId > 0);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertNotEquals(0, result.getResponse().getContentLength());

        // Clean
        this.petDocumentDAO.delete(document);
        this.petDAO.delete(pet);
    }

    @Test
    @DisplayName("Download Document - Failed")
    public void testDownloadDocumentFailed() throws Exception {
        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8081/pasms-server/pet-document-api/download/" + -1))
                .andReturn();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(0, result.getResponse().getContentLength());
    }

    @Test
    @DisplayName("Find all pet documents")
    public void testFindAllPetDocuments() throws Exception {
        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");
        int petId = petDAO.create(pet);

        PetDocument document = new PetDocument(petId, "text/plain", "test.txt", new byte[]{1, 2, 3});
        int documentId = petDocumentDAO.create(document);
        document.setId(documentId);

        PetDocument document2 = new PetDocument(petId, "text/plain", "test2.txt", new byte[]{-1, -2, -3});
        int documentId2 = petDocumentDAO.create(document2);
        document2.setId(documentId2);

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8081/pasms-server/pet-document-api/findAllDocuments/" + petId))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<PetDocument> petDocumentList = objectMapper.readValue(jsonResponse, new TypeReference<List<PetDocument>>() {});


        // Assert
        assertTrue(petId > 0);
        assertTrue(documentId > 0);
        assertTrue(documentId2 > 0);
        assertEquals(HttpStatus.OK.value(), status);
        assertEquals(2, petDocumentList.size());
        assertTrue(petDocumentList.contains(document));
        assertTrue(petDocumentList.contains(document2));

        // Clean
        this.petDocumentDAO.delete(document);
        this.petDocumentDAO.delete(document2);
        this.petDAO.delete(pet);
    }

}
