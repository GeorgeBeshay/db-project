package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.PetDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PetDAO petDAO;

    public PetService(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
    }

    public int createPet(Pet pet) {
        int petCreatedId = petDAO.create(pet);
        if (petCreatedId > 0)
            Logger.logMsgFrom(this.getClass().getName(), "A pet is created successfully", 0);

        else
            Logger.logMsgFrom(this.getClass().getName(), "A pet is failed to be created", 1);

        return petCreatedId;
    }

    public boolean updatePet(Pet pet) {
        boolean petUpdated = petDAO.update(pet);
        if (petUpdated)
            Logger.logMsgFrom(this.getClass().getName(), "A pet is updated successfully", 0);

        else
            Logger.logMsgFrom(this.getClass().getName(), "A pet is failed to be updated", 1);

        return petUpdated;
    }

    public boolean deletePet(Pet pet) {
        boolean petDeleted = petDAO.delete(pet);
        if (petDeleted)
            Logger.logMsgFrom(this.getClass().getName(), "A pet is deleted successfully", 0);

        else
            Logger.logMsgFrom(this.getClass().getName(), "A pet is failed to be deleted", 1);

        return petDeleted;
    }

}
