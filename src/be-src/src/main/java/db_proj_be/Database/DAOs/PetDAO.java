package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

    @Transactional
    public List<Pet> getUnAdoptedPets() {
        try {
            String sql = "EXEC GetUnAdoptedPets";
            return jdbcTemplate.query(sql, rowMapper);
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in getting the unAdopted pets" + e.getMessage(), -1);
            return null;
        }
    }

    public List<Pet> getPetsWithOptionsAndSorted(List<Integer> shelterIds, Map<String, Object> criteria, List<String> orderByColumns) {
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM PET P ");
            sql.append("LEFT JOIN ADOPTION A ON P.id = A.pet_id WHERE A.pet_id IS NULL ");
            MapSqlParameterSource parameters = new MapSqlParameterSource();

            if (shelterIds != null && !shelterIds.isEmpty()) {
                sql.append("AND P.shelter_id IN (:shelterIds) ");
                parameters.addValue("shelterIds", shelterIds);
            }

            if (criteria != null) {
                criteria.forEach((key, value) -> {
                    sql.append(" AND P.").append(key).append(" = :").append(key);
                    parameters.addValue(key, value);
                });
            }

            if (orderByColumns != null && !orderByColumns.isEmpty()) {
                sql.append(" ORDER BY ").append(String.join(", ", orderByColumns));
            }

            return namedParameterJdbcTemplate.query(sql.toString(), parameters, rowMapper);
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in getting pets filtered and sorted" + e.getMessage(), -1);
            return null;
        }
    }
}