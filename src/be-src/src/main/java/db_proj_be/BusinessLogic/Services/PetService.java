package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.EntityModels.PetAvailabilityNotification;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.AdopterDAO;
import db_proj_be.Database.DAOs.PetAvailabilityNotificationDAO;
import db_proj_be.Database.DAOs.PetDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class PetService {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PetDAO petDAO;
    private final AdopterDAO adopterDAO;
    private final PetAvailabilityNotificationDAO petAvailabilityNotificationDAO;

    public PetService(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        this.adopterDAO = new AdopterDAO(jdbcTemplate);
        this.petAvailabilityNotificationDAO = new PetAvailabilityNotificationDAO(jdbcTemplate);
    }

    public int createPet(Pet pet) {
        int petCreatedId = petDAO.create(pet);
        if (petCreatedId > 0) {
            Logger.logMsgFrom(this.getClass().getName(), "A pet is created successfully", 0);
            this.notifyAdopters(petCreatedId);
        } else {
            Logger.logMsgFrom(this.getClass().getName(), "A pet is failed to be created", 1);
        }

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

    public List<Pet> getUnAdoptedPets() {
        List<Pet> unAdoptedPets = petDAO.getUnAdoptedPets();
        if (unAdoptedPets != null)
            Logger.logMsgFrom(this.getClass().getName(), "Getting unAdopted pets successfully..", 0);

        else
            Logger.logMsgFrom(this.getClass().getName(), "Getting unAdopted pets failed..", 1);

        return unAdoptedPets;
    }

    public Pet findById(int petId) {
        Pet pet = petDAO.findById(petId);
        if (pet != null)
            Logger.logMsgFrom(this.getClass().getName(), "A pet is found successfully", 0);

        else
            Logger.logMsgFrom(this.getClass().getName(), "A pet is failed to be found", 1);

        return pet;
    }

    public void notifyAdopters(int petId) {
        List<Integer> adoptersIds = this.adopterDAO.getAdoptersIDs();

        for (int adopterId : adoptersIds) {
            PetAvailabilityNotification petAvailabilityNotification =
                    new PetAvailabilityNotification(petId, adopterId, false, new Date(System.currentTimeMillis()));

            boolean isCreated = this.petAvailabilityNotificationDAO.create(petAvailabilityNotification);

            if (isCreated)
                Logger.logMsgFrom(this.getClass().getName(), "Successfully created pet availability notification", 0);
            else
                Logger.logMsgFrom(this.getClass().getName(), "Failed to create pet availability notification", 1);

        }
    }
}
