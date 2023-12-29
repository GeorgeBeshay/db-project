package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Adoption;
import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
import db_proj_be.BusinessLogic.EntityModels.ApplicationNotification;
import db_proj_be.BusinessLogic.EntityModels.ApplicationStatus;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.AdoptionApplicationDAO;
import db_proj_be.Database.DAOs.AdoptionDAO;
import db_proj_be.Database.DAOs.ApplicationNotificationDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class StaffService {

    private final AdoptionApplicationDAO adoptionApplicationDAO;
    private final AdoptionDAO adoptionDAO;
    private final ApplicationNotificationDAO applicationNotificationDAO;

    public StaffService(JdbcTemplate jdbcTemplate) {
        this.adoptionApplicationDAO = new AdoptionApplicationDAO(jdbcTemplate);
        this.adoptionDAO = new AdoptionDAO(jdbcTemplate);
        this.applicationNotificationDAO = new ApplicationNotificationDAO(jdbcTemplate);
    }

    public List<AdoptionApplication> fetchAllAdoptionApplications() {
        List<AdoptionApplication> adoptionApplications = this.adoptionApplicationDAO.findAll();

        if (adoptionApplications != null)
            Logger.logMsgFrom(this.getClass().getName(), "Successfully fetched all adoption applications", 0);
        else
            Logger.logMsgFrom(this.getClass().getName(), "Failed to fetch all adoption applications", 1);

        return adoptionApplications;
    }

    public List<AdoptionApplication> fetchAdoptionApplicationsByStatus(ApplicationStatus status) {
        List<AdoptionApplication> adoptionApplications = this.adoptionApplicationDAO.findByStatus(status);

        if (adoptionApplications != null)
            Logger.logMsgFrom(this.getClass().getName(), "Successfully fetched " + status + " adoption applications", 0);
        else
            Logger.logMsgFrom(this.getClass().getName(), "Failed to fetch " + status + " adoption applications", 1);

        return adoptionApplications;
    }

    public boolean updateApplication(AdoptionApplication adoptionApplication) {
        boolean isUpdated = this.adoptionApplicationDAO.update(adoptionApplication);

        if (isUpdated) {
            Logger.logMsgFrom(this.getClass().getName(), "Successfully updated adoption application with id "
                    + adoptionApplication.getId(), 0);

            // Create a notification record
            this.notifyAdopter(adoptionApplication.getId(), adoptionApplication.getAdopterId());

            // If approved, create adoption record
            if (adoptionApplication.getStatus() == ApplicationStatus.APPROVED)
                return this.createAdoptionRecord(adoptionApplication.getPetId(), adoptionApplication.getAdopterId());
        } else {
            Logger.logMsgFrom(this.getClass().getName(), "Failed to update adoption application with id "
                    + adoptionApplication.getId(), 1);
        }

        return isUpdated;
    }

    public boolean createAdoptionRecord(int petId, int adopterId) {
        Adoption adoption = new Adoption(petId, adopterId);

        boolean isCreated = this.adoptionDAO.create(adoption);

        if (isCreated) {
            Logger.logMsgFrom(this.getClass().getName(), "Successfully created adoption record", 0);
        } else {
            Logger.logMsgFrom(this.getClass().getName(), "Failed to create adoption record", 1);
        }

        return isCreated;
    }

    public void notifyAdopter(int appId, int adopterId) {
        ApplicationNotification applicationNotification =
                new ApplicationNotification(appId, adopterId, false, new Date(System.currentTimeMillis()));

        boolean isCreated = this.applicationNotificationDAO.create(applicationNotification);

        if (isCreated) {
            Logger.logMsgFrom(this.getClass().getName(), "Successfully created application notification", 0);
        } else {
            Logger.logMsgFrom(this.getClass().getName(), "Failed to create application notification", 1);
        }
    }
}
