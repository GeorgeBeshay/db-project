package db_proj_be.Database.DAOs;

import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ShelterDAO extends DAO<Shelter>{
    public ShelterDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Shelter.class, "SHELTER");
    }
    @Transactional
    public Shelter findByName(String name){
        if(name == null){
            throw new IllegalArgumentException("Shelter name to find can't be null");
        }
        try{
            String sql = "SELECT * FROM SHELTER WHERE name = >";
            return jdbcTemplate.queryForObject(sql,Shelter.class);
        }catch (Exception e){
            return null;
        }
    }
    @Transactional
    public List<Shelter> findByLocation(String location){
        if(location == null){
            throw new IllegalArgumentException("Shelter location to find can't be null");
        }
        try{
            String sql = "SELECT * FROM SHELTER WHERE location = >";
            return jdbcTemplate.queryForList(sql,Shelter.class);
        }catch (Exception e){
            return null;
        }
    }
    @Transactional
    public boolean deleteByName(String name){
        if(name == null){
            throw new IllegalArgumentException("Shelter name to be deleted can't be null");
        }
        try {
            String sql = "DELETE FROM SHELTER WHERE name = ?";
            int rowsAffected = jdbcTemplate.update(sql,rowMapper,name);
            return rowsAffected>0;
        }catch (Exception e){
            return false;
        }
    }

    @Transactional
    public boolean update(Shelter shelter) {
        try {
            String sql = """
                    UPDATE SHELTER
                    SET name = ?, location = ?, email = ?,
                    phone = ?, manager = ?
                    WHERE id = ?
                    """;
            int rowsAffected = jdbcTemplate.update(sql, shelter.getName(), shelter.getLocation(), shelter.getEmail(), shelter.getPhone(), shelter.getManager(), shelter.getId());

            return rowsAffected > 0;
        }

        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in updating the shelter: " + e.getMessage(), 1);
            return false;
        }
    }
}
