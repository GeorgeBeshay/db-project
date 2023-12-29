package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Adoption;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class StaffDAO extends DAO<Staff>{
    public StaffDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Staff.class, "STAFF");
    }

    public boolean update(Staff staff){
        if(staff == null){
            throw new IllegalArgumentException("Staff object to be updated can't be null");
        }
        try {
            String sql = """
                    UPDATE STAFF
                    SET first_name = ?, last_name = ?, role = ?, phone = ?, email = ?, password_hash = ?, shelter_id = ? 
                    WHERE id = ?""";
            int rowsAffected = jdbcTemplate.update(sql,staff.getFirstName(),staff.getLastName(),staff.getRole(),staff.getPhone(),staff.getEmail(),staff.getPaswordHash(),staff.getShelterId(),staff.getId());
            return rowsAffected > 0;
        }catch (Exception e){
            Logger.logMsgFrom(this.getClass().getName(),"Error in updating Staff member "+e.getMessage(),1);
            return false;
        }
    }
}
