package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Adopter;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data Access Object (DAO) for managing Adopter entities.
 * Extends the abstract DAO class to provide specific operations for Adopter entities.
 */
public class AdopterDAO extends DAO<Adopter> {

    /**
     * Constructs an AdopterDAO instance with the provided JdbcTemplate.
     * @param jdbcTemplate The JdbcTemplate used for database interactions.
     */
    public AdopterDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Adopter.class, "ADOPTER");
    }

    /**
     * Updates an Adopter entity in the database.
     * @param adopter The Adopter entity to be updated.
     * @return True if the update is successful, false otherwise.
     * @throws IllegalArgumentException If adopter is null.
     */
    @Transactional
    public boolean update(Adopter adopter) {
        // guard clause
        if (adopter == null) {
            throw new IllegalArgumentException("Can't update an entity given a null object ..");
        }

        try {
            String sql = "UPDATE ADOPTER "
                    + "SET first_name = ?, "
                    + "last_name = ?, "
                    + "email = ?, "
                    + "password_hash = ?, "
                    + "phone = ?, "
                    + "birth_date = ?, "
                    + "gender = ?, "
                    + "address = ? "
                    + "WHERE id = ?";

            return jdbcTemplate.update(
                    sql,
                    adopter.getFirstName(),
                    adopter.getLastName(),
                    adopter.getEmail(),
                    adopter.getPasswordHash(),
                    adopter.getPhone(),
                    adopter.getBirthDate(),
                    adopter.getGender(),
                    adopter.getAddress(),
                    adopter.getId()
            ) > 0;

        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTER update(): " + ex.getMessage(), 1);
            return false; // Return a meaningful response indicating failure

        }

    }

    @Transactional
    public Adopter findByEmail(String email) {

        try {
            String sql = "SELECT * FROM ADOPTER WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, email);

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTER findById(): " + e.getMessage(), 1);
            return null;

        }

    }

}
