package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.PetAvailabilityNotification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PetAvailabilityNotificationDAO {

    private final JdbcTemplate jdbcTemplate;

    private final BeanPropertyRowMapper<PetAvailabilityNotification> rowMapper;

    public PetAvailabilityNotificationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(PetAvailabilityNotification.class);
    }

    // Return all rows in the table
    @Transactional
    public List<PetAvailabilityNotification> findAll() {
        try {
            String sql = "SELECT * FROM PET_AVAILABILITY_NOTIFICATION";
            return jdbcTemplate.query(sql, rowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Return all records with the given pet id
    @Transactional
    public List<PetAvailabilityNotification> findByPetId(int petId) {
        try {
            String sql = "SELECT * FROM PET_AVAILABILITY_NOTIFICATION WHERE pet_id = ?";
            return jdbcTemplate.query(sql, rowMapper, petId);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all records with the given adopter id
    @Transactional
    public List<PetAvailabilityNotification> findByAdopterId(int adopterId) {
        try {
            String sql = "SELECT * FROM PET_AVAILABILITY_NOTIFICATION WHERE adopter_id = ?";
            return jdbcTemplate.query(sql, rowMapper, adopterId);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all records with the given status value
    @Transactional
    public List<PetAvailabilityNotification> findByStatus(Boolean status) {
        try {
            String sql = "SELECT * FROM PET_AVAILABILITY_NOTIFICATION WHERE status = ?";
            return jdbcTemplate.query(sql, rowMapper, status);
        } catch (Exception e) {
            return null;
        }
    }

    // Save the given availability notification in the database table
    @Transactional
    public boolean create(PetAvailabilityNotification petAvailabilityNotification) {
        // Guard check for null
        if (petAvailabilityNotification == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }

        try {
            String relationName = "PET_AVAILABILITY_NOTIFICATION";
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(relationName);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(petAvailabilityNotification);
            return jdbcInsert.execute(parameterSource) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update the status of the given application notification in the database table
    @Transactional
    public boolean update(PetAvailabilityNotification petAvailabilityNotification) {
        // Guard check for null
        if (petAvailabilityNotification == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }

        try {
            String sql = "UPDATE PET_AVAILABILITY_NOTIFICATION" +
                    "SET status = ?" +
                    " WHERE pet_id = ? and adopter_id = ?";
            return jdbcTemplate.update(sql, petAvailabilityNotification.getStatus(),
                    petAvailabilityNotification.getPetId(), petAvailabilityNotification.getAdopterId()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete the notification record with the
    // given primary key (pet_id, adopter_id)
    // Return true if a record was deleted,
    // and false if no rows where affected
    @Transactional
    public boolean delete(PetAvailabilityNotification petAvailabilityNotification) {
        // Guard check for null
        if (petAvailabilityNotification == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }

        try {
            String sql = "DELETE FROM PET_AVAILABILITY_NOTIFICATION WHERE pet_id = ? and adopter_id = ?";
            return jdbcTemplate.update(sql, petAvailabilityNotification.getPetId(),
                    petAvailabilityNotification.getAdopterId()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
