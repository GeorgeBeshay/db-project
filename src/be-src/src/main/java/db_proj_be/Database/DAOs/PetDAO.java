package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

public class PetDAO {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final BeanPropertyRowMapper<Pet> rowMapper;

    public PetDAO (JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(Pet.class);
        this.rowMapper.setPrimitivesDefaultedForNullValue(true);
    }

    public BeanPropertyRowMapper<Pet> getRowMapper() {
        return rowMapper;
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
            System.out.println("The pet can not be updated: " + e.getMessage());
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
            System.out.println("Can not get the pets ordered by birthdate: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<Pet> filterByVaccination(boolean vaccination) {
        try {
            String sql = """
                    SELECT *
                    FROM PET
                    WHERE vaccination = ?
                    """;

            return jdbcTemplate.query(sql, rowMapper, vaccination);
        }

        catch (Exception e) {
            System.out.println("Can not filter the pets by vaccination: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<Pet> filterByHouseTraining(boolean houseTraining) {
        try {
            String sql = """
                    SELECT *
                    FROM PET
                    WHERE house_training = ?
                    """;

            return jdbcTemplate.query(sql, rowMapper, houseTraining);
        }

        catch (Exception e) {
            System.out.println("Can not filter the pets by house training: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<Pet> filterByNeutering(boolean neutering) {
        try {
            String sql = """
                    SELECT *
                    FROM PET
                    WHERE neutering = ?
                    """;

            return jdbcTemplate.query(sql, rowMapper, neutering);
        }

        catch (Exception e) {
            System.out.println("Can not filter the pets by neutering: " + e.getMessage());
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
            System.out.println("Can not get the pets filtered by some attributes: " + e.getMessage());
            return null;
        }
    }
}
