package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.Database.DAOs.PetDAO;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
@AutoConfigureMockMvc
public class PetAPITests {
    private final MockMvc mockMvc;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PetDAO petDAO;

    @Autowired
    public PetAPITests(MockMvc mockMvc, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create Pet - Successful Pet Creation")
    public void testCreatePetWithValidObject() throws Exception {

        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setBirthdate("2018-5-6");
        pet.setHealthStatus("test");

        // Perform the HTTP request to create a pet
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/pet-api/createPet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pet)))
                .andReturn();

        int status = result.getResponse().getStatus();
        int petCreatedId = Integer.parseInt(result.getResponse().getContentAsString());

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(petCreatedId >= 1);

        // Clean
        pet.setId(petCreatedId);
        this.petDAO.delete(pet);
    }

    @Test
    @DisplayName("Create Pet - Failed Pet Creation")
    public void testCreatePetWithInValidObject() throws Exception {

        // Arrange
        Pet pet = new Pet();

        // Perform the HTTP request to create a pet
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/pet-api/createPet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pet)))
                .andReturn();

        int status = result.getResponse().getStatus();
        int petCreatedId = Integer.parseInt(result.getResponse().getContentAsString());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals(-1, petCreatedId);
    }

    @Test
    @DisplayName("Update Pet - Successful Pet Updating")
    public void testUpdatePetWithValidObject() throws Exception {

        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setBirthdate("2018-5-6");
        pet.setHealthStatus("test");

        int petId = this.petDAO.create(pet);
        pet.setId(petId);
        pet.setName("cat");

        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/pet-api/updatePet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pet)))
                .andReturn();

        boolean isSuccess = Boolean.parseBoolean(result.getResponse().getContentAsString());
        int status = result.getResponse().getStatus();

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(isSuccess);

        // Clean
        this.petDAO.delete(pet);
    }

    @Test
    @DisplayName("update Pet - Failed Pet updating")
    public void testUpdatePetWithInValidObject() throws Exception {

        // Arrange
        Pet pet = new Pet();
        pet.setId(-1);

        // Perform the HTTP request to create a pet
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/pet-api/updatePet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pet)))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean isSuccess = Boolean.parseBoolean(result.getResponse().getContentAsString());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Delete Pet - Successful Pet deletion")
    public void testDeletePetWithValidObject() throws Exception {

        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setBirthdate("2018-5-6");
        pet.setHealthStatus("test");

        int petId = this.petDAO.create(pet);
        pet.setId(petId);

        // Perform the HTTP request to create a pet
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/pet-api/deletePet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pet)))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean isSuccess = Boolean.parseBoolean(result.getResponse().getContentAsString());

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Delete Pet - Failed Pet deletion")
    public void testDeletePetWithInValidObject() throws Exception {

        // Arrange
        Pet pet = new Pet();
        pet.setId(-1);

        // Perform the HTTP request to create a pet
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/pet-api/deletePet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pet)))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean isSuccess = Boolean.parseBoolean(result.getResponse().getContentAsString());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Get unAdopted Pets - Successful")
    public void testGetUnAdoptedPets() throws Exception {

        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setHealthStatus("test");

        int petId = this.petDAO.create(pet);
        pet.setId(petId);

        // Perform the HTTP request to create a pet
        MvcResult result = mockMvc.perform(get("http://localhost:8081/pasms-server/pet-api/getUnAdoptedPets"))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Pet> petList = objectMapper.readValue(jsonResponse, new TypeReference<List<Pet>>() {});

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(petList.size() > 0);
        assertTrue(petList.contains(pet));

        // Clean
        this.petDAO.delete(pet);
    }

    @Test
    @DisplayName("FindById Pet - Successful")
    public void testFindByIdWithValidObject() throws Exception {

        // Arrange
        Pet pet = new Pet();
        pet.setName("Dog");
        pet.setSpecie("test");
        pet.setBreed("test");
        pet.setGender(true);
        pet.setBirthdate("2018-5-6");
        pet.setHealthStatus("test");

        int petId = this.petDAO.create(pet);
        pet.setId(petId);

        // Perform the HTTP request to create a pet
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/pet-api/findPetById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(petId)))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Pet returnedPet = objectMapper.readValue(jsonResponse, new TypeReference<Pet>() {});

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertEquals(returnedPet.getId(), petId);
        assertEquals(returnedPet.getName(), pet.getName());
    }

    @Test
    @DisplayName("FindById Pet - Failed")
    public void testFindByIdWithInValidId() throws Exception {

        // Perform the HTTP request to create a pet
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/pet-api/findPetById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(-1)))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals(jsonResponse, "");
    }

}
