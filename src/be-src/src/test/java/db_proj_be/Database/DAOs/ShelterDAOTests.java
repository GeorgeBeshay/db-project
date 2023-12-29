package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class ShelterDAOTests {

    private final JdbcTemplate jdbcTemplate;
    private ShelterDAO shelterDAO;

    @Autowired
    public ShelterDAOTests(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void setup(){
        shelterDAO = new ShelterDAO(jdbcTemplate);
    }

    @Test
    public void testFindByName() {
        // Arrange
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter expectedShelter = new Shelter();
        expectedShelter.setName(shelterName);
        expectedShelter.setLocation(shelterLocation);
        expectedShelter.setPhone(shelterPhone);
        expectedShelter.setEmail(shelterEmail);

        shelterId = shelterDAO.create(expectedShelter);
        assertTrue(shelterId >= 1);
        expectedShelter.setId(shelterId);

        // Act
        List<Shelter> actualShelters = shelterDAO.findByName(shelterName);

        // Assert
        assertTrue(actualShelters.contains(expectedShelter));

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    public void testFindByLocation() {
        // Arrange
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter expectedShelter = new Shelter();
        expectedShelter.setName(shelterName);
        expectedShelter.setLocation(shelterLocation);
        expectedShelter.setPhone(shelterPhone);
        expectedShelter.setEmail(shelterEmail);

        shelterId = shelterDAO.create(expectedShelter);
        assertTrue(shelterId >= 1);
        expectedShelter.setId(shelterId);

        // Act
        List<Shelter> actualShelters = shelterDAO.findByLocation(shelterLocation);

        // Assert
        assertTrue(actualShelters.contains(expectedShelter));

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    public void testDeleteByName() {
        // Arrange
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter expectedShelter = new Shelter();
        expectedShelter.setName(shelterName);
        expectedShelter.setLocation(shelterLocation);
        expectedShelter.setPhone(shelterPhone);
        expectedShelter.setEmail(shelterEmail);

        shelterId = shelterDAO.create(expectedShelter);
        assertTrue(shelterId >= 1);
        expectedShelter.setId(shelterId);

        // Act
        boolean deletionResult = shelterDAO.deleteByName(shelterName);

        // Assert
        assertTrue(deletionResult);
        assertNull(shelterDAO.findById(shelterId));
    }

    @Test
    public void testUpdate() {
        // Arrange
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter expectedShelter = new Shelter();
        expectedShelter.setName(shelterName);
        expectedShelter.setLocation(shelterLocation);
        expectedShelter.setPhone(shelterPhone);
        expectedShelter.setEmail(shelterEmail);

        shelterId = shelterDAO.create(expectedShelter);
        assertTrue(shelterId >= 1);
        expectedShelter.setId(shelterId);

        // modify the attributes
        String modifiedShelterLocation = "SHELTER X NEW LOCATION";
        String modifiedShelterName = "SHELTER X MODIFIED";
        expectedShelter.setLocation(modifiedShelterLocation);
        expectedShelter.setName(modifiedShelterName);

        // Act
        boolean updateResult = shelterDAO.update(expectedShelter);

        // Assert
        assertTrue(updateResult);
        Shelter retrievedShelter = shelterDAO.findById(shelterId);
        assertEquals(expectedShelter, retrievedShelter);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

}
