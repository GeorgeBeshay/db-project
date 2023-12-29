package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.Database.DAOs.AdminDAO;
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

    private MockMvc mockMvc;
    private JdbcTemplate jdbcTemplate;
    private AdminDAO adminDAO;

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
    public void testAdminSignInWithValidObject() throws Exception {

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
}
