package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Adopter;
import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.AdopterDAO;
import db_proj_be.Database.DAOs.AdoptionApplicationDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdopterService {

    private final AdoptionApplicationDAO adoptionApplicationDAO;
    private final AdopterDAO adopterDAO;

    public AdopterService(JdbcTemplate jdbcTemplate) {
        this.adoptionApplicationDAO = new AdoptionApplicationDAO(jdbcTemplate);
        this.adopterDAO = new AdopterDAO(jdbcTemplate);
    }

    public int createAdoptionApplication(AdoptionApplication adoptionApplication) {
        int adoptionAppId = this.adoptionApplicationDAO.create(adoptionApplication);
        if (adoptionAppId > 0)
            Logger.logMsgFrom(this.getClass().getName(), "Adoption application is created successfully", 0);
        else
            Logger.logMsgFrom(this.getClass().getName(), "Adoption application failed to create", 1);
        return adoptionAppId;
    }

    public List<AdoptionApplication> fetchAdoptionApplications(int adopterId) {
        List<AdoptionApplication> adoptionApplications = this.adoptionApplicationDAO.findByAdopterId(adopterId);
        if (adoptionApplications != null)
            Logger.logMsgFrom(this.getClass().getName(), "Successfully fetched adoption applications for adopter " + adopterId, 0);
        else
            Logger.logMsgFrom(this.getClass().getName(), "Failed to fetch adoption applications for adopter " + adopterId, 1);
        return adoptionApplications;
    }

    public Adopter findById(int adopterId) {
        Adopter adopter = this.adopterDAO.findById(adopterId);
        if (adopter != null)
            Logger.logMsgFrom(this.getClass().getName(), "Successfully fetched adopter with id " + adopterId, 0);
        else
            Logger.logMsgFrom(this.getClass().getName(), "Failed to fetch adopter " + adopterId, 1);
        return adopter;
    }

    public Adopter signInLogic(Adopter actualAdopter) {

        if (actualAdopter == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Adopter object can't be null.", 1);
            return null;
        }

        Adopter expectedAdopter = adopterDAO.findByEmail(actualAdopter.getEmail());

        if (expectedAdopter == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Adopter record doesn't exist.", 1);
            return null;
        }

        if (!Hasher.hash(actualAdopter.getPasswordHash()).equals(expectedAdopter.getPasswordHash())) {
            Logger.logMsgFrom(this.getClass().getName(), "Adopter Authentication Failure.", 1);
            return null;
        }

        if (Hasher.hash(actualAdopter.getPasswordHash()).equals(expectedAdopter.getPasswordHash())) {
            Logger.logMsgFrom(this.getClass().getName(), "Adopter Successful Authentication", 0);
            return expectedAdopter;
        }

        Logger.logMsgFrom(this.getClass().getName(), "Something had went wrong ..", 1);
        return null;
    }

    public Adopter signUpLogic(Adopter newAdopter) {

        if (newAdopter == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Adopter object can't be null.", 1);
            return null;
        }

        // hash the password.
        newAdopter.setPasswordHash(Hasher.hash(newAdopter.getPasswordHash()));

        int adopterId = this.adopterDAO.create(newAdopter);

        if (adopterId >= 1) {
            Logger.logMsgFrom(this.getClass().getName(), "Adopter Record Created Successfully with ID = " +
                    adopterId, 0);
            return this.adopterDAO.findById(adopterId);
        }

        Logger.logMsgFrom(this.getClass().getName(), "Something had went wrong ..", 1);
        return null;
    }

}
