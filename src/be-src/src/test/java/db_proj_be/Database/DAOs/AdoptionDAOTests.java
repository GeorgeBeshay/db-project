package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class AdoptionDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AdoptionDAO adoptionDAO;

    @BeforeEach
    public void initAdoptionDAO() {
        // enforce independence between the tests.
        this.adoptionDAO = new AdoptionDAO(jdbcTemplate);
    }

    @AfterAll
    public void finishTests() {
        Logger.logMsgFrom(this.getClass().getName(), "AdopterDAOTests finished.", -1);
    }

}
