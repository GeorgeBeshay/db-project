package db_proj_be.APIs;

import com.fasterxml.jackson.databind.ObjectMapper;
import db_proj_be.BusinessLogic.EntityModels.*;
import db_proj_be.Database.DAOs.*;
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

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
@AutoConfigureMockMvc
public class NotificationAPITests {

    private final MockMvc mockMvc;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public NotificationAPITests(MockMvc mockMvc, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private int petId;
    private int adopterId;
    private int adoptionApplicationId;

    @BeforeAll
    public void prepareDatabase() {
        this.petId = this.createPet();
        this.adopterId = this.createAdopter();
        this.adoptionApplicationId = this.createAdoptionApplication();
    }

    // Creation methods for referenced entities to satisfy foreign key constraints
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

    private int createAdopter() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "John.Doe234@example.com";
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

        assertTrue(adopterId > 0);

        return adopterId;
    }

    private int createAdoptionApplication() {
        // Arrange
        int adopterId = this.adopterId;
        int petId = this.petId;
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

        int appId = new AdoptionApplicationDAO(jdbcTemplate).create(adoptionApplication);
        assertTrue(appId > 0);
        return appId;
    }

    @AfterAll
    public void clean() {
        jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", this.adoptionApplicationId);
        jdbcTemplate.update("DELETE FROM PET WHERE id = ?", this.petId);
        jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", this.adopterId);
    }

    @AfterAll
    public void finishTests() {
        System.out.println("ApplicationNotificationDAOTests finished.");
    }

    // Helper method to convert Map to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------- Tests ----------------

    @Test
    @DisplayName("Updating status of application notification - Successful")
    public void testUpdateAppNotSuccessful() throws Exception {
        // Arrange
        int appId = this.adoptionApplicationId;
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Act
        ApplicationNotificationDAO applicationNotificationDAO = new ApplicationNotificationDAO(jdbcTemplate);
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertTrue(isSuccess);

        // modify the record
        boolean newStatus = true;
        applicationNotification.setStatus(newStatus);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/notification-api/updateAppNot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(applicationNotification)))
                .andReturn();

        // Retrieve the response status code
        int responseStatus = result.getResponse().getStatus();

        // Assert the status code and Admin object
        assertEquals(HttpStatus.OK.value(), responseStatus);
        assertEquals("true", result.getResponse().getContentAsString());

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId));
    }

    @Test
    @DisplayName("Updating status of application notification - Failure")
    public void testUpdateAppNotFailed() throws Exception {
        // Arrange
        int appId = this.adoptionApplicationId;
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // modify the record
        boolean newStatus = true;
        applicationNotification.setStatus(newStatus);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/notification-api/updateAppNot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(applicationNotification)))
                .andReturn();

        // Retrieve the response status code
        int responseStatus = result.getResponse().getStatus();

        // Assert the status code and Admin object
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseStatus);
        assertEquals("false", result.getResponse().getContentAsString());

        // Clean
        assertEquals(0, jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ?", appId));
    }

    @Test
    @DisplayName("Updating status of pet availability notification - Successful")
    public void testUpdatePetNotSuccessful() throws Exception {
        // Arrange
        int petId = this.petId;
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // Act
        PetAvailabilityNotificationDAO petAvailabilityNotificationDAO =
                new PetAvailabilityNotificationDAO(jdbcTemplate);
        boolean isSuccess = petAvailabilityNotificationDAO.create(petAvailabilityNotification);

        // Assert
        assertTrue(isSuccess);

        // modify the record
        boolean newStatus = true;
        petAvailabilityNotification.setStatus(newStatus);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/notification-api/updatePetNot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(petAvailabilityNotification)))
                .andReturn();

        // Retrieve the response status code
        int responseStatus = result.getResponse().getStatus();

        // Assert the status code and Admin object
        assertEquals(HttpStatus.OK.value(), responseStatus);
        assertEquals("true", result.getResponse().getContentAsString());

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE pet_id = ? and adopter_id = ?", petId, adopterId));
    }

    @Test
    @DisplayName("Updating status of pet availability notification - Failure")
    public void testUpdatePetNotFailed() throws Exception {
        // Arrange
        int petId = this.petId;
        int adopterId = this.adopterId;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        PetAvailabilityNotification petAvailabilityNotification = new PetAvailabilityNotification(petId, adopterId, status, date);

        // modify the record
        boolean newStatus = true;
        petAvailabilityNotification.setStatus(newStatus);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/notification-api/updatePetNot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(petAvailabilityNotification)))
                .andReturn();

        // Retrieve the response status code
        int responseStatus = result.getResponse().getStatus();

        // Assert the status code and Admin object
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseStatus);
        assertEquals("false", result.getResponse().getContentAsString());

        // Clean
        assertEquals(0, jdbcTemplate.update("DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE pet_id = ? and adopter_id = ?", petId, adopterId));
    }

}

