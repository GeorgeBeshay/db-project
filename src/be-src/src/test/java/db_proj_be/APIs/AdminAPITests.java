package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.EntityModels.StaffRole;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.Database.DAOs.AdminDAO;
import db_proj_be.Database.DAOs.ShelterDAO;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
@AutoConfigureMockMvc
public class AdminAPITests {

    private final MockMvc mockMvc;
    private final JdbcTemplate jdbcTemplate;
    private final AdminDAO adminDAO;

    @Autowired
    public AdminAPITests(MockMvc mockMvc, JdbcTemplate jdbcTemplate) {
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
        this.adminDAO = new AdminDAO(jdbcTemplate);
    }

    // Helper method to convert Map to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // --------------------- Sign In Tests ---------------------

    @Test
    @DisplayName("Admin Sign In - Successful Authentication")
    public void testSignInWithValidObject() throws Exception {

        int id = -100; // ignored
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedpassword";
        String phone = "123-456-7890";

        Admin expectedAdmin = new Admin(id, firstName, lastName, email, phone, Hasher.hash(passwordHash));
        id = this.adminDAO.create(expectedAdmin);
        expectedAdmin.setPasswordHash(passwordHash);
        expectedAdmin.setId(id);
        assertTrue(id >= 1);

        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/admin-api/adminSignIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedAdmin)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content as an Admin object
        Admin resultAdmin = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Admin.class);

        // Assert the status code and Admin object
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(resultAdmin);
        expectedAdmin.setPasswordHash(Hasher.hash(passwordHash));
        assertEquals(expectedAdmin, resultAdmin);

        // Clean
        assertTrue(this.adminDAO.delete(expectedAdmin));

    }

    @Test
    @DisplayName("Admin Creating Staff - Successful Creation")
    public void testCreateValidStaff() throws Exception {
        // Arrange

        // creating shelter
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // prepare a staff object
        int staffId = -1; // will be ignored.
        String firstName = "John";
        String lastName = "Doe";
        StaffRole role = StaffRole.MANAGER;
        String email = "johnDoe@gmail.com";
        String passwordHash = "This is the password hash";

        Staff staff = new Staff();
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setRole(role.getValue());
        staff.setEmail(email);
        staff.setPasswordHash(passwordHash);
        staff.setShelterId(shelterId);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/admin-api/createStaff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content as an Admin object
        Staff resultStaff = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Staff.class);

        // Assert the status code and Admin object
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(resultStaff);
        staffId = resultStaff.getId();
        staff.setId(staffId);
        staff.setPasswordHash(Hasher.hash(passwordHash));
        assertEquals(staff, resultStaff);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Admin Creating Staff - Invalid Creation")
    public void testCreateInvalidStaff() throws Exception {
        // Arrange

        // creating shelter
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // prepare a staff object
        int staffId = -1; // will be ignored.
        String firstName = "John";
        String lastName = "Doe";
        StaffRole role = StaffRole.MANAGER;
        String email = "johnDoe@gmail.com";
        String passwordHash = "This is the password hash";

        Staff staff = new Staff();
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setRole(role.getValue());
        staff.setEmail(email);
        staff.setPasswordHash(passwordHash);
        staff.setShelterId(shelterId);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/admin-api/createStaff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content as an Admin object
        Staff resultStaff = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Staff.class);

        // Assert the status code and Admin object
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(resultStaff);
        staffId = resultStaff.getId();
        staff.setId(staffId);
        staff.setPasswordHash(Hasher.hash(passwordHash));
        assertEquals(staff, resultStaff);

        // Try to create a duplicate (because they have the same email.)
        result = mockMvc.perform(post("http://localhost:8081/pasms-server/admin-api/createStaff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        status = result.getResponse().getStatus();

        // Assert
        assertEquals("", result.getResponse().getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

}
