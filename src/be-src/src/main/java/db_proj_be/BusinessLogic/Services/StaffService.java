package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.StaffDAO;
import org.springframework.jdbc.core.JdbcTemplate;

public class StaffService {

    private final JdbcTemplate jdbcTemplate;
    private final StaffDAO staffDAO;

    public StaffService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.staffDAO = new StaffDAO(jdbcTemplate);
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

}
