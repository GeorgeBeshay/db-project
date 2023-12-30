package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.ApplicationNotification;
import db_proj_be.BusinessLogic.EntityModels.PetAvailabilityNotification;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.ApplicationNotificationDAO;
import db_proj_be.Database.DAOs.PetAvailabilityNotificationDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final ApplicationNotificationDAO applicationNotificationDAO;
    private final PetAvailabilityNotificationDAO petAvailabilityNotificationDAO;

    public NotificationService(JdbcTemplate jdbcTemplate) {
        this.applicationNotificationDAO = new ApplicationNotificationDAO(jdbcTemplate);
        this.petAvailabilityNotificationDAO = new PetAvailabilityNotificationDAO(jdbcTemplate);
    }

    public boolean updateAppNotification(ApplicationNotification applicationNotification) {
        boolean isUpdated = applicationNotificationDAO.update(applicationNotification);
        if (isUpdated)
            Logger.logMsgFrom(this.getClass().getName(), "App notification is updated successfully", 0);

        else
            Logger.logMsgFrom(this.getClass().getName(), "App notification is failed to be updated", 1);

        return isUpdated;
    }

    public boolean updatePetNotification(PetAvailabilityNotification petAvailabilityNotification) {
        boolean isUpdated = petAvailabilityNotificationDAO.update(petAvailabilityNotification);
        if (isUpdated)
            Logger.logMsgFrom(this.getClass().getName(), "Pet notification is updated successfully", 0);

        else
            Logger.logMsgFrom(this.getClass().getName(), "Pet notification is failed to be updated", 1);

        return isUpdated;
    }
}
