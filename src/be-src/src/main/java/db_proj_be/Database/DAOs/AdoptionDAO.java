package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Adoption;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AdoptionDAO {

    private final JdbcTemplate jdbcTemplate;
    protected final BeanPropertyRowMapper<Adoption> rowMapper;

    public AdoptionDAO(JdbcTemplate jdbcTemplate) {
        if (jdbcTemplate == null) {
            throw new IllegalArgumentException("JdbcTemplate Object shouldn't be a null object.");
        }

        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(Adoption.class);
        this.rowMapper.setPrimitivesDefaultedForNullValue(true);
    }

    @Transactional
    public List<Adoption> findAll() {

        try {
            String sql = "SELECT * FROM ADOPTION";
            return jdbcTemplate.query(sql, rowMapper);

        }
        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION findAll(): " + e.getMessage(), 1);
            return null;

        }

    }

    @Transactional
    public Adoption findByPetId(int petId) {

        try {
            String sql = "SELECT * FROM ADOPTION WHERE pet_id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, petId);

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION findByPetId(): " + e.getMessage(), 1);
            return null;

        }

    }

    @Transactional
    public List<Adoption> findByAdopterId(int adopterId) {

        try {
            String sql = "SELECT * FROM ADOPTION WHERE adopter_id = ?";
            return jdbcTemplate.query(sql, rowMapper, adopterId);

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION findByPetId(): " + e.getMessage(), 1);
            return null;

        }

    }

    @Transactional
    public boolean create(Adoption adoption) {

        if (adoption == null) {
            throw new IllegalArgumentException("Entity object to create can't be null");
        }

        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("ADOPTION");
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(adoption);

            return jdbcInsert.execute(parameterSource) > 0;

        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION create(): " + ex.getMessage(), 1);
            return false; // Return a meaningful response indicating failure

        }

    }

    @Transactional
    public boolean delete(Adoption adoption) {

        // guard clause
        if (adoption == null) {
            throw new IllegalArgumentException("Entity object to delete can't be null");
        }
        return delete(adoption.getPetId(), adoption.getAdopterId());

    }

    public boolean delete(int petId, int adopterId) {

        try {
            String sql = "DELETE FROM ADOPTION WHERE adopter_id = ? AND pet_id = ?";
            return jdbcTemplate.update(sql, adopterId, petId) > 0;

        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in ADOPTION delete(): " + ex.getMessage(), 1);
            return false; // Return a meaningful response indicating failure

        }

    }

}
