package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data Access Object (DAO) for managing Admin entities.
 * Extends the abstract DAO class to provide specific operations for Admin entities.
 */
public class AdminDAO extends DAO<Admin> {

    /**
     * Constructs an AdminDAO instance with the provided JdbcTemplate.
     * @param jdbcTemplate The JdbcTemplate used for database interactions.
     */
    public AdminDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Admin.class, "ADMINISTRATOR");
    }

    /**
     * Updates an Admin entity in the database.
     * @param admin The Admin entity to be updated.
     * @return True if the update is successful, false otherwise.
     * @throws IllegalArgumentException If admin is null.
     */
    @Transactional
    public boolean update(Admin admin) {
        // guard clause
        if (admin == null) {
            throw new IllegalArgumentException("Can't update an entity given a null object ..");
        }

        try {
            String sql = "UPDATE ADMINISTRATOR "
                    + "SET first_name = ?, "
                    + "last_name = ?, "
                    + "email = ?, "
                    + "password_hash = ?, "
                    + "phone = ?"
                    + "WHERE id = ?";

            return jdbcTemplate.update(
                    sql,
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getEmail(),
                    admin.getPasswordHash(),
                    admin.getPhone(),
                    admin.getId()
            ) > 0;

        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADMINISTRATOR update(): " + ex.getMessage(), 1);
            return false; // Return a meaningful response indicating failure

        }

    }

}
