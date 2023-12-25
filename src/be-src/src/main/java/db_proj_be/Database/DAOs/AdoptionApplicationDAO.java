package db_proj_be.Database.DAOs;


import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
import db_proj_be.BusinessLogic.EntityModels.ApplicationStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public class AdoptionApplicationDAO {

    private final JdbcTemplate jdbcTemplate;

    private final BeanPropertyRowMapper<AdoptionApplication> rowMapper;

    public AdoptionApplicationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(AdoptionApplication.class);
    }

    // Return all rows in the table
    @Transactional
    public List<AdoptionApplication> findAll() {
        String sql = "SELECT * FROM ADOPTION_APPLICATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // Return the adoption application record for the given id
    @Transactional
    public AdoptionApplication findById(int id) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Return all records with the given status value
    @Transactional
    public List<AdoptionApplication> findByStatus(ApplicationStatus status) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE status = ?";
            return jdbcTemplate.query(sql, rowMapper, status);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all records with the given creation date
    @Transactional
    public List<AdoptionApplication> findByDateCreated(Date date) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE date = ?";
            return jdbcTemplate.query(sql, rowMapper, date);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all application records for the given adopter id
    @Transactional
    public List<AdoptionApplication> findByAdopterId(int id) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE adopter_id = ?";
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all application records for the given pet id
    @Transactional
    public List<AdoptionApplication> findByPetId(int id) {
        try {
            String sql = "SELECT * FROM ADOPTION_APPLICATION WHERE pet_id = ?";
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    // Save the given adoption application in the database table
    @Transactional
    public boolean create(AdoptionApplication adoptionApplication) {
        try {
            String relationName = "ADOPTION_APPLICATION";
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(relationName);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(adoptionApplication);
            return jdbcInsert.execute(parameterSource) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update the values of the adoption application with new values
    @Transactional
    public boolean update(AdoptionApplication adoptionApplication) {
        try {
            String sql = "UPDATE ADOPTION_APPLICATION SET " +
                    "adopter_id = ?, pet_id = ?, status = ?, " +
                    "description = ?, experience = ?, creation_date = ?, " +
                    "closing_date = ? WHERE id = ?";
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete the application record with the given id
    // Return true if a record was deleted,
    // and false if no rows where affected
    @Transactional
    public boolean delete(AdoptionApplication adoptionApplication) {
        try {
            String sql = "DELETE FROM ADOPTION_APPLICATION WHERE id = ? ";
            return jdbcTemplate.update(sql, adoptionApplication.getId()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
