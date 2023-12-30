package db_proj_be.APIs;

import com.fasterxml.jackson.databind.ObjectMapper;
import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.EntityModels.StaffRole;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.Database.DAOs.ShelterDAO;
import db_proj_be.Database.DAOs.StaffDAO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
@AutoConfigureMockMvc
public class StaffAPITests {

    private final MockMvc mockMvc;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StaffAPITests(MockMvc mockMvc, JdbcTemplate jdbcTemplate) {
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
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
    @DisplayName("Staff Updating his / her profile - Successful")
    public void testUpdateStaffSuccessful() throws Exception {
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

        StaffDAO staffDAO = new StaffDAO(jdbcTemplate);
        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // modify the record
        StaffRole newRole = StaffRole.MEMBER;
        String modifiedEmail = "johnDoeModified@gmail.com";
        staff.setRole(newRole.getValue());
        staff.setEmail(modifiedEmail);


        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/updateStaff")
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
        assertEquals(staff, resultStaff);

        Staff retrievedStaffFromDB = staffDAO.findById(staffId);
        assertEquals(staff, retrievedStaffFromDB);
        assertEquals(resultStaff, retrievedStaffFromDB);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Updating his / her profile - Failure")
    public void testUpdateStaffFailure() throws Exception {
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

        StaffDAO staffDAO = new StaffDAO(jdbcTemplate);
        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // modify the record
        String newRole = "INVALID ROLE";
        staff.setRole(newRole);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/updateStaff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        staff.setRole(role.getValue()); // reset the object to its initial state before modification.
        Staff retrievedStaffFromDB = staffDAO.findById(staffId);
        assertEquals(staff, retrievedStaffFromDB);

        // try to modify another attribute with some invalid value
        int newShelterId = -1;
        staff.setShelterId(newShelterId);

        // Act again
        result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/updateStaff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        status = result.getResponse().getStatus();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        staff.setShelterId(shelterId); // reset the object to its initial state before modification.
        retrievedStaffFromDB = staffDAO.findById(staffId);
        assertEquals(staff, retrievedStaffFromDB);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Sign in - Successful")
    public void testSignInSuccessful() throws Exception {
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
        staff.setPasswordHash(Hasher.hash(passwordHash));
        staff.setShelterId(shelterId);

        StaffDAO staffDAO = new StaffDAO(jdbcTemplate);
        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // use the original password
        staff.setPasswordHash(passwordHash);

        // suppose that the ID is unknown
        staff.setId(-1);


        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/sign-in")
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
        staff.setPasswordHash(Hasher.hash(passwordHash));
        staff.setId(staffId);
        assertEquals(staff, resultStaff);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Sign in - Failure")
    public void testSignInFailure() throws Exception {
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
        staff.setPasswordHash(Hasher.hash(passwordHash));
        staff.setShelterId(shelterId);

        StaffDAO staffDAO = new StaffDAO(jdbcTemplate);
        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // use the original password
        staff.setPasswordHash(passwordHash + "modified");

        // suppose that the ID is unknown
        staff.setId(-1);


        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Assert the status code and Admin object
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

}
