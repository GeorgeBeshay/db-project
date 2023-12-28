package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.ApplicationNotification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public class ApplicationNotificationDAO {

    private final JdbcTemplate jdbcTemplate;

    private final BeanPropertyRowMapper<ApplicationNotification> rowMapper;

    public ApplicationNotificationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(ApplicationNotification.class);
    }

    // Return all rows in the table
    @Transactional
    public List<ApplicationNotification> findAll() {
        try {
            String sql = "SELECT * FROM APPLICATION_NOTIFICATION";
            return jdbcTemplate.query(sql, rowMapper);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all records with the given application id
    @Transactional
    public List<ApplicationNotification> findByAppId(int appId) {
        try {
            String sql = "SELECT * FROM APPLICATION_NOTIFICATION WHERE application_id = ?";
            return jdbcTemplate.query(sql, rowMapper, appId);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all records with the given adopter id
    @Transactional
    public List<ApplicationNotification> findByAdopterId(int adopterId) {
        try {
            String sql = "SELECT * FROM APPLICATION_NOTIFICATION WHERE adopter_id = ?";
            return jdbcTemplate.query(sql, rowMapper, adopterId);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all records with the given status value
    @Transactional
    public List<ApplicationNotification> findByStatus(Boolean status) {
        try {
            String sql = "SELECT * FROM APPLICATION_NOTIFICATION WHERE status = ?";
            return jdbcTemplate.query(sql, rowMapper, status);
        } catch (Exception e) {
            return null;
        }
    }

    // Return all records with the given date
    @Transactional
    public List<ApplicationNotification> findByDate(Date date) {
        try {
            String sql = "SELECT * FROM APPLICATION_NOTIFICATION WHERE date = ?";
            return jdbcTemplate.query(sql, rowMapper, date);
        } catch (Exception e) {
            return null;
        }
    }

    // Save the given application notification in the database table
    @Transactional
    public boolean create(ApplicationNotification applicationNotification) {
        // Guard check for null
        if (applicationNotification == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }

        try {
            String relationName = "APPLICATION_NOTIFICATION";
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(relationName);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(applicationNotification);
            return jdbcInsert.execute(parameterSource) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete the notification record with the
    // given primary key (adoption_app_id, adopter_id)
    // Return true if a record was deleted,
    // and false if no rows where affected or exception was thrown
    @Transactional
    public boolean delete(ApplicationNotification applicationNotification) {
        // Guard check for null
        if (applicationNotification == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }

        try {
            String sql = "DELETE FROM APPLICATION_NOTIFICATION WHERE application_id = ? and adopter_id = ?";
            return jdbcTemplate.update(sql, applicationNotification.getApplicationId(),
                    applicationNotification.getAdopterId()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
