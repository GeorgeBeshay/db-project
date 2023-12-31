package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class StaffDAO extends DAO<Staff>{
    public StaffDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Staff.class, "STAFF");
    }

    @Transactional
    public boolean update(Staff staff){

        if(staff == null){
            throw new IllegalArgumentException("Staff object to be updated can't be null");
        }

        try {

            String sql =
                    "UPDATE STAFF " +
                    "SET " +
                            "first_name = ?, " +
                            "last_name = ?, " +
                            "role = ?, " +
                            "phone = ?, " +
                            "email = ?, " +
                            "password_hash = ?, " +
                            "shelter_id = ?" +
                    "WHERE id = ?;";

            int rowsAffected = jdbcTemplate.update(
                    sql,
                    staff.getFirstName(),
                    staff.getLastName(),
                    staff.getRole(),
                    staff.getPhone(),
                    staff.getEmail(),
                    staff.getPasswordHash(),
                    staff.getShelterId(),
                    staff.getId()
            );

            return rowsAffected > 0;

        } catch (Exception e){
            Logger.logMsgFrom(this.getClass().getName(),"Error in updating Staff member " + e.getMessage(),1);
            return false;

        }
    }

    @Transactional
    public Staff findByEmail(String email) {

        try {
            String sql = "SELECT * FROM STAFF WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, email);

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in STAFF findByEmail(): " + e.getMessage(), 1);
            return null;

        }

    }

}
