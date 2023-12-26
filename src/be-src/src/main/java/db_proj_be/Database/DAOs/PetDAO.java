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
    public List<Pet> sortByBirthdate() {
        try {
            String sql = """
                    SELECT *
                    FROM PET
                    ORDER BY birthdate
                    """;

            return jdbcTemplate.query(sql, rowMapper);
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in sorting the pets with their birthdate: " + e.getMessage(), 1);
            return null;
        }
    }

    @Transactional
    public List<Pet> findByAttributes(Map<String, Object> attributesToValues) {
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM PET WHERE 1=1 ");
            Map<String, Object> parametersMap = new HashMap<>();

            attributesToValues.forEach((key, value) -> {
                sql.append("AND ").append(key).append(" = :").append(key).append(" ");
                parametersMap.put(key, value);
            });

            return namedParameterJdbcTemplate.query(sql.toString(), parametersMap, rowMapper);
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in filtering the pets: " + e.getMessage(), 1);
            return null;
        }
    }
}
