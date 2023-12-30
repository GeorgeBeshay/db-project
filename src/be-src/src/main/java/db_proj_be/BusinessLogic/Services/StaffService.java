package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.*;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.AdoptionApplicationDAO;
import db_proj_be.Database.DAOs.AdoptionDAO;
import db_proj_be.Database.DAOs.ApplicationNotificationDAO;
import db_proj_be.Database.DAOs.StaffDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class StaffService {

    private final AdoptionApplicationDAO adoptionApplicationDAO;
    private final AdoptionDAO adoptionDAO;
    private final ApplicationNotificationDAO applicationNotificationDAO;
    private final StaffDAO staffDAO;

    public StaffService(JdbcTemplate jdbcTemplate) {
        this.adoptionApplicationDAO = new AdoptionApplicationDAO(jdbcTemplate);
        this.adoptionDAO = new AdoptionDAO(jdbcTemplate);
        this.applicationNotificationDAO = new ApplicationNotificationDAO(jdbcTemplate);
        this.staffDAO = new StaffDAO(jdbcTemplate);
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

    /**
     * Updates an existing staff member record.
     *
     * @param staff Staff object containing updated details.
     * @return Updated Staff object, or null if update fails or the record is not found.
     */
    public Staff updateStaff(Staff staff) {

        if (staff == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff object can't be null.", 1);
            return null;
        }

        // check that the given staff object id, is actually in the DB.
        Staff staffRecordToBeUpdated = this.staffDAO.findById(staff.getId());
        if (staffRecordToBeUpdated == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff record couldn't be found.", 1);
            return null;
        }

        // the update operation is to fail either if a SQL error had occurred, or if the record was not modified.
        boolean updateResult = this.staffDAO.update(staff);
        Staff staffRecordAfterBeingUpdated = this.staffDAO.findById(staff.getId());
        updateResult = updateResult && (!staffRecordAfterBeingUpdated.equals(staffRecordToBeUpdated));
        if (!updateResult) {
            Logger.logMsgFrom(this.getClass().getName(), "Failed to update the staff record.", 1);
            return null;
        }

        Staff updatedStaffRecord = this.staffDAO.findById(staff.getId());
        if (updatedStaffRecord == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Something had went wrong ..", 1);
            return null;
        }

        return updatedStaffRecord;
    }

    /**
     * Validates and authenticates staff member login based on provided credentials.
     *
     * @param actualStaff The Staff object containing login credentials. (email and password)
     * @return The authenticated Staff object if successful, null otherwise.
     */
    public Staff signInLogic(Staff actualStaff) {
        if (actualStaff == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff object can't be null.", 1);
            return null;
        }

        Staff expectedStaff = staffDAO.findByEmail(actualStaff.getEmail());

        if (expectedStaff == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff record doesn't exist.", 1);
            return null;
        }

        if (!Hasher.hash(actualStaff.getPasswordHash()).equals(expectedStaff.getPasswordHash())) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff Authentication Failure.", 1);
            return null;
        }

        if (Hasher.hash(actualStaff.getPasswordHash()).equals(expectedStaff.getPasswordHash())) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff Successful Authentication", 0);
            return expectedStaff;
        }

        Logger.logMsgFrom(this.getClass().getName(), "Something had went wrong ..", 1);
        return null;
    }

}
