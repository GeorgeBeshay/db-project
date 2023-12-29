package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.EntityModels.StaffRole;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class StaffDAOTests {

    private final JdbcTemplate jdbcTemplate;
    private StaffDAO staffDAO;

    @Autowired
    public StaffDAOTests(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.staffDAO = new StaffDAO(jdbcTemplate);
    }

    @BeforeEach
    public void setup(){
        staffDAO = new StaffDAO(jdbcTemplate);
    }

    @Test
    @DisplayName("Staff DAO - Update")
    public void testUpdate(){
        // Arrange
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

        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // Make a change to the staff record.
        StaffRole newRole = StaffRole.MANAGER;
        String modifiedEmail = "modified@mail.com";

        staff.setRole(newRole.getValue());
        staff.setEmail(modifiedEmail);

        // Act
        boolean updateResult = staffDAO.update(staff);

        // Assert
        assertTrue(updateResult);
        Staff modifiedStaff = staffDAO.findById(staffId);
        assertEquals(staff, modifiedStaff);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

}
