package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.AdminDAO;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdminServices {
    private final JdbcTemplate jdbcTemplate;
    private final AdminDAO adminDAO;
//    private final StaffDAO staffDAO;

    public AdminServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.adminDAO = new AdminDAO(jdbcTemplate);
    }

    public Admin SignInLogic(Admin actualAdmin) {

        if (actualAdmin == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Admin object can't be null.", 1);
            return null;
        }

        Admin expectedAdmin = adminDAO.findById(actualAdmin.getId());

        if (expectedAdmin == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Admin record doesn't exist.", 1);
            return null;
        }

        if (!Hasher.hash(actualAdmin.getPasswordHash()).equals(expectedAdmin.getPasswordHash())) {
            Logger.logMsgFrom(this.getClass().getName(), "Admin Authentication Failure.", 1);
            return null;
        }

        if (Hasher.hash(actualAdmin.getPasswordHash()).equals(expectedAdmin.getPasswordHash())) {
            Logger.logMsgFrom(this.getClass().getName(), "Admin Successful Authentication", 0);
            return expectedAdmin;
        }

        Logger.logMsgFrom(this.getClass().getName(), "Something had went wrong ..", 1);
        return null;
    }

    public Staff createStaff(Staff staff) {

        if (staff == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff object can't be null.", 1);
            return null;
        }

//        int createdStaffId = this.staffDAO.create(staff);
        int createdStaffId = -1;

        if (createdStaffId < 1) {
            Logger.logMsgFrom(this.getClass().getName(), "Failed to create a staff record.", 1);
            return null;
        }

//        Staff createdStaff = this.staffDAO.findById(createdStaffId);
        Staff createdStaff = null;

        if (createdStaff != null) {
            Logger.logMsgFrom(this.getClass().getName(), "Created a staff record with id " + createdStaffId + "successfully.", 0);
            return createdStaff;
        }

        Logger.logMsgFrom(this.getClass().getName(), "Something had went wrong ..", 1);
        return null;
    }

    public Staff updateStaff(Staff staff) {

        if (staff == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff object can't be null.", 1);
            return null;
        }

//        Staff staffRecordToBeUpdated = this.staffDAO.findById(staff.getId())
        Staff staffRecordToBeUpdated = null;

        if (staffRecordToBeUpdated == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff record couldn't be found.", 1);
            return null;
        }



        return null;
    }

}
