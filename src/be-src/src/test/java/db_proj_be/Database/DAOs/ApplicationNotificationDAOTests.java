package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.ApplicationNotification;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class ApplicationNotificationDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ApplicationNotificationDAO applicationNotificationDAO;

    @BeforeEach
    public void initAdopterDAOTest() {
        // enforce independence between the tests.
        this.applicationNotificationDAO = new ApplicationNotificationDAO(jdbcTemplate);
    }

    @AfterAll
    public void finishTests() {
        System.out.println("ApplicationNotificationDAOTests finished.");
    }

    // ------------------------- Creation Tests -------------------------

    @Test
    public void testApplicationNotificationCreationAllArguments() {
        // Arrange
        int appId = 30_000;
        int adopterId = 20_000;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Act
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertTrue(isSuccess);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId, appId);
    }

    @Test
    public void testApplicationNotificationCreationDuplicateObjects() {
        // Arrange
        int appId = 30_001;
        int adopterId = 20_001;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Act
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertTrue(isSuccess);
        isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertFalse(isSuccess);

        // Clean (to prevent modifying the DB current state)
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId, appId);
    }

    @Test
    public void testApplicationNotificationCreationMissingAttributes() {
        // Arrange
        int adopterId = 20_003;
        int appId = 30_003;

        ApplicationNotification applicationNotification = new ApplicationNotification();
        applicationNotification.setAdopterId(adopterId);
        applicationNotification.setApplicationId(appId);


        // Act
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    public void testApplicationNotificationCreationNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> applicationNotificationDAO.create(null));
    }

    // ------------------------- Deletion Tests -------------------------

    @Test
    public void testApplicationNotificationDeletionNullObject() {
        // Arrange - none

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> applicationNotificationDAO.delete(null));
    }

    @Test
    public void testApplicationNotificationDeletionObjectDoesntExist() {
        // Arrange - none
        ApplicationNotification applicationNotification = new ApplicationNotification();
        applicationNotification.setApplicationId(20_004);
        applicationNotification.setAdopterId(30_004);

        // Act
        boolean isSuccess = applicationNotificationDAO.delete(applicationNotification);

        // Assert
        assertFalse(isSuccess);
    }

    @Test
    public void testApplicationNotificationDeletionValidObject() {
        // Arrange
        int appId = 30_005;
        int adopterId = 20_005;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        // Act
        isSuccess = applicationNotificationDAO.delete(applicationNotification);

        // Assert
        assertTrue(isSuccess);
    }

    // ------------------------- Find Tests -------------------------

    @Test
    public void testApplicationNotificationFindAdopterIdDoesntExist() {
        // Arrange - none
        int adopterId = 20_008;

        // Act & Assert
        assertTrue(applicationNotificationDAO.findByAdopterId(adopterId).isEmpty());
    }

    @Test
    public void testApplicationNotificationFindAdopterIdValid() {
        int appId = 30_006;
        int adopterId = 20_006;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        // Act
        List<ApplicationNotification> fetchedAppNot = applicationNotificationDAO.findByAdopterId(adopterId);

        // Assert
        assertTrue(fetchedAppNot.size() >= 1);
        assertTrue(fetchedAppNot.contains(applicationNotification));

        // clean
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId, appId);
    }

    @Test
    public void testApplicationNotificationFindAppIdDoesntExist() {
        // Arrange - none
        int appId = 30_008;

        // Act & Assert
        assertTrue(applicationNotificationDAO.findByAppId(appId).isEmpty());
    }

    @Test
    public void testApplicationNotificationFindAppIdValid() {
        // Arrange
        int appId = 30_007;
        int adopterId = 20_007;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        // Act
        List<ApplicationNotification> fetchedAppNot = applicationNotificationDAO.findByAppId(appId);

        // Assert
        assertTrue(fetchedAppNot.size() >= 1);
        assertTrue(fetchedAppNot.contains(applicationNotification));

        // clean
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId, appId);
    }

    @Test
    public void testApplicationNotificationFindStatusValid() {
        // Arrange
        int appId = 30_009;
        int adopterId = 20_009;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        int appId2 = 30_010;
        int adopterId2 = 20_010;
        boolean status2 = true;
        Date date2 = Date.valueOf("2006-03-15");

        ApplicationNotification applicationNotification2 = new ApplicationNotification(appId2, adopterId2, status2, date2);

        // Add the record to DB
        isSuccess = applicationNotificationDAO.create(applicationNotification2);
        assertTrue(isSuccess);

        // Act
        List<ApplicationNotification> fetchedAppNot = applicationNotificationDAO.findByStatus(status);

        // Assert
        assertTrue(fetchedAppNot.size() >= 1);
        assertTrue(fetchedAppNot.contains(applicationNotification));

        fetchedAppNot = applicationNotificationDAO.findByStatus(status2);

        // Assert
        assertTrue(fetchedAppNot.size() >= 1);
        assertTrue(fetchedAppNot.contains(applicationNotification2));

        // clean
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId, appId);
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId2, appId2);
    }

    @Test
    public void testApplicationNotificationFindAll() {
        // Arrange
        int appId = 30_011;
        int adopterId = 20_011;
        boolean status = false;
        Date date = Date.valueOf("2001-12-15");

        ApplicationNotification applicationNotification = new ApplicationNotification(appId, adopterId, status, date);

        // Add the record to DB
        boolean isSuccess = applicationNotificationDAO.create(applicationNotification);
        assertTrue(isSuccess);

        int appId2 = 30_012;
        int adopterId2 = 20_012;
        boolean status2 = true;
        Date date2 = Date.valueOf("2006-03-15");

        ApplicationNotification applicationNotification2 = new ApplicationNotification(appId2, adopterId2, status2, date2);

        // Add the record to DB
        isSuccess = applicationNotificationDAO.create(applicationNotification2);
        assertTrue(isSuccess);

        // Act
        List<ApplicationNotification> fetchedAppNot = applicationNotificationDAO.findAll();

        // Assert
        assertTrue(fetchedAppNot.size() >= 2);
        assertTrue(fetchedAppNot.contains(applicationNotification));
        assertTrue(fetchedAppNot.contains(applicationNotification2));

        // clean
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId, appId);
        jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ? and application_id = ?", adopterId2, appId2);
    }

}
