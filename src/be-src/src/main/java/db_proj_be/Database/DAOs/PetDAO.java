package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

public class PetDAO extends DAO<Pet> {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public PetDAO (JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, Pet.class, "PET");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    public boolean update(Pet pet) {
        try {
            String sql = """
                    UPDATE PET
                    SET name = ?, specie = ?, breed = ?,
                    birthdate = ?, gender = ?, health_status = ?,
                    behaviour = ?, description = ?, shelter_id = ?, neutering = ?,
                    house_training = ?, vaccination = ?
                    WHERE id = ?
                    """;
            int rowsAffected = jdbcTemplate.update(sql, pet.getName(), pet.getSpecie(),
                    pet.getBreed(), pet.getBirthdate(), pet.isGender(), pet.getHealthStatus(),
                    pet.getBehaviour(), pet.getDescription(), pet.getShelterId(), pet.isNeutering(),
                    pet.isHouseTraining(), pet.isVaccination(), pet.getId());

            return rowsAffected > 0;
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in updating the pet: " + e.getMessage(), 1);
            return false;
        }
    }

    @Transactional
    public List<Pet> sortByAttribute(String attribute, boolean ascending) {
        List<String> allowedColumns = Arrays.asList("id", "name", "specie", "breed", "birthdate",
                "gender", "health_status", "behaviour", "description", "shelter_id", "neutering",
                "house_training", "vaccination");

        if (!allowedColumns.contains(attribute)) {
            throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }

        try {
            String sortOrder = ascending ? "ASC" : "DESC";

            String sql = "SELECT * FROM PET ORDER BY " + attribute + " " + sortOrder;

            return jdbcTemplate.query(sql, rowMapper);
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in sorting the pets: " + e.getMessage(), 1);
            return null;
        }
    }

    /**
     * This function filter the pets according to the map attributesToValues
     * This function selects the pets that satisfy all equality conditions of attributes map
     * In case the map is empty, this function will return all pets in the DB
     * @param attributesToValues this will map each attribute to its value like: "specie" -> "dog"
     * @return list of pets that satisfy all equality conditions
     */
    @Transactional
    public List<Pet> findByAttributes(Map<String, Object> attributesToValues) {

        if (attributesToValues == null) {
            throw new IllegalArgumentException("Map object can't be null");
        }

        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM PET WHERE 1=1 ");

            attributesToValues.forEach((key, value) -> {
                sql.append("AND ").append(key).append(" = :").append(key).append(" ");
            });

            return namedParameterJdbcTemplate.query(sql.toString(), attributesToValues, rowMapper);
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in filtering the pets: " + e.getMessage(), 1);
            return null;
        }
    }
}