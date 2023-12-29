package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
public class StaffDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StaffDAO staffDAO;

    @BeforeAll
    public void setup(){
        staffDAO = new StaffDAO(jdbcTemplate);
    }
    @Test
    @DisplayName("Staff DAO - Find staff by ID")
    public void testFindById(){

        int id = staffDAO.create(new Staff(1,"John","Doe","Test","","johndoe@gmail.com",13214,3));
        assertTrue(id >= 1);
        jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?",1);
    }
}
