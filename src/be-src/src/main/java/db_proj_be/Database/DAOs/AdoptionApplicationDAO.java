package db_proj_be.Database.DAOs;


import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
import db_proj_be.BusinessLogic.EntityModels.ApplicationStatus;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public class AdoptionApplicationDAO extends DAO<AdoptionApplication> {

    private final BeanPropertyRowMapper<AdoptionApplication> rowMapper;

    public AdoptionApplicationDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, AdoptionApplication.class, "ADOPTION_APPLICATION");
        this.rowMapper = new BeanPropertyRowMapper<>(AdoptionApplication.class);
    }

    // Return all records with the given status value
    @Transactional
    public List<AdoptionApplication> findByStatus(ApplicationStatus status) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE status = ?";
            return jdbcTemplate.query(sql, getRowMapper(), status.getValue());
        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION_APPLICATION findByStatus(): " + ex.getMessage(), 1);
            return null;
        }
    }

    // Return all records with the given creation date
    @Transactional
    public List<AdoptionApplication> findByDateCreated(Date date) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE date = ?";
            return jdbcTemplate.query(sql, getRowMapper(), date);
        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION_APPLICATION findByDateCreated(): " + ex.getMessage(), 1);
            return null;
        }
    }

    // Return all application records for the given adopter id
    @Transactional
    public List<AdoptionApplication> findByAdopterId(int id) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE adopter_id = ?";
            return jdbcTemplate.query(sql, getRowMapper(), id);
        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION_APPLICATION findByAdopterId(): " + ex.getMessage(), 1);
            return null;
        }
    }

    // Return all application records for the given pet id
    @Transactional
    public List<AdoptionApplication> findByPetId(int id) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE pet_id = ?";
            return jdbcTemplate.query(sql, getRowMapper(), id);
        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION_APPLICATION findByPetId(): " + ex.getMessage(), 1);
            return null;
        }
    }

    // Update the values of the adoption application with new values
    @Transactional
    public boolean update(AdoptionApplication adoptionApplication) {
        // guard clause
        if (adoptionApplication == null) {
            throw new IllegalArgumentException("Can't update an entity given a null object ..");
        }
        
        try {
            String sql = "UPDATE ADOPTION_APPLICATION SET " +
                    "adopter_id = ?, " +
                    "pet_id = ?, " +
                    "status = ?, " +
                    "description = ?, " +
                    "experience = ?, " +
                    "creation_date = ?, " +
                    "closing_date = ? " +
                    "WHERE id = ?";
            return jdbcTemplate.update(sql,
                    adoptionApplication.getAdopterId(),
                    adoptionApplication.getPetId(),
                    adoptionApplication.getStatus().getValue(),
                    adoptionApplication.getDescription(),
                    adoptionApplication.getExperience(),
                    adoptionApplication.getCreationDate(),
                    adoptionApplication.getClosingDate(),
                    adoptionApplication.getId()
            ) > 0;
        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION_APPLICATION update(): " + ex.getMessage(), 1);
            return false;
        }
    }
}
