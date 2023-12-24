package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Identifiable;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

/*
 * Important Assumptions and Notes
 * -------------------------------
 * 1. This DAO implements the CRUD operations on a relations that is identified by a single "id" attribute.
 * 2. In case of the need to customize some specific methods, overload the method.
 * 3. In case of the need to disallow a specific functionality that is provided by this abstract DAO that can't be
 * offered by the child DAO you are implementing, you have to override it and implement some assertions or conditions
 * that cancel the abstract functionality, this can simply be just an empty function.
 */

/**
 * Abstract Data Access Object (DAO) providing CRUD operations for entities identified by an "id" attribute.
 * Extend this class to implement specific DAOs for entities.
 */
public abstract class DAO <T extends Identifiable> {
    protected JdbcTemplate jdbcTemplate;
    private Class<T> clazz;
    protected BeanPropertyRowMapper<T> rowMapper;
    private String relationName;

    public DAO(){}

    /**
     * Constructor to initialize the DAO with necessary attributes.
     * @param jdbcTemplate The JdbcTemplate used for database interactions.
     * @param clazz The class type representing the entity.
     * @param relationName The name of the database relation/table for the entity.
     * @throws IllegalArgumentException If jdbcTemplate, clazz, or relationName is null/empty.
     */
    public DAO(JdbcTemplate jdbcTemplate, Class<T> clazz, String relationName) {
        //
        if (jdbcTemplate == null || clazz == null) {
            throw new IllegalArgumentException("Abstract DAO attributes must be not null.");
        }
        if (Objects.equals(relationName, "")) {
            throw new IllegalArgumentException("Abstract DAO relationName can't be an empty string.");
        }

        this.jdbcTemplate = jdbcTemplate;
        this.clazz = clazz;
        this.rowMapper = new BeanPropertyRowMapper<>(clazz);
        this.rowMapper.setPrimitivesDefaultedForNullValue(true);
        this.relationName = relationName;
    }

    /**
     * Retrieves all entities of type T from the database.
     * @return List of entities of type T if successful, null otherwise.
     */
    @Transactional
    public List<T> findAll() {

        try {
            String sql = "SELECT * FROM " + relationName;
            return jdbcTemplate.query(sql, rowMapper);

        }
        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in " + relationName + " findAll(): " + e.getMessage(), 1);
            return null;

        }

    }

    /**
     * Retrieves an entity of type T by its id from the database.
     * @param id The id of the entity to retrieve.
     * @return Entity of type T if found, null otherwise.
     */
    @Transactional
    public T findById(int id) {

        try {
            String sql = "SELECT * FROM " + relationName + " WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in " + relationName + " findById(): " + e.getMessage(), 1);
            return null;

        }

    }

    /**
     * Creates a new entity in the database.
     * @param objOfTypeT The entity object to create.
     * @return True if creation is successful, false otherwise.
     * @throws IllegalArgumentException If objOfTypeT is null.
     */
    @Transactional
    public boolean create(T objOfTypeT) {
        // guard clause
        if (objOfTypeT == null) {
            throw new IllegalArgumentException("Entity object to create can't be null");
        }

        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(relationName);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(objOfTypeT);

            return jdbcInsert.execute(parameterSource) > 0;

        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in " + relationName + " create(): " + ex.getMessage(), 1);
            return false; // Return a meaningful response indicating failure

        }

    }

    /**
     * Deletes an entity from the database.
     * @param objOfTypeT The entity object to delete.
     * @return True if deletion is successful, false otherwise.
     * @throws IllegalArgumentException If objOfTypeT is null.
     */
    @Transactional
    public boolean delete(T objOfTypeT) {
        // guard clause
        if (objOfTypeT == null) {
            throw new IllegalArgumentException("Entity object to delete can't be null");
        }

        try {
            String sql = "DELETE FROM " + relationName + " WHERE id = ?";
            return jdbcTemplate.update(sql, objOfTypeT.getId()) > 0;

        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in " + relationName + " delete(): " + ex.getMessage(), 1);
            return false; // Return a meaningful response indicating failure

        }

    }

    /**
     * Retrieves the class type of the entity.
     * @return The class type of the entity.
     */
    public Class<T> getClazz() {
        return clazz;
    }

    /**
     * Retrieves the row mapper configured for the current instance of a child dao, for mapping rows to entity objects.
     * @return The BeanPropertyRowMapper for the entity.
     */
    public BeanPropertyRowMapper<T> getRowMapper() {
        return rowMapper;
    }

}
