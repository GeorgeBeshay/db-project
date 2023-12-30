package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.AdminDAO;
import db_proj_be.Database.DAOs.ShelterDAO;
import db_proj_be.Database.DAOs.StaffDAO;
import org.apache.juli.logging.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class providing methods to handle administrative functionalities.
 * 1. Manages authentication and operations related to administrators.
 * 2. Manages administrative operations such as creating a staff or a shelter.
 */

@Service
public class AdminService {
    private final JdbcTemplate jdbcTemplate;
    private final AdminDAO adminDAO;
    private final StaffDAO staffDAO;
    private final ShelterDAO shelterDAO;

    /**
     * Constructor for AdminServices class initializing necessary dependencies.
     *
     * @param jdbcTemplate JDBC template for database interaction.
     */
    public AdminService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.adminDAO = new AdminDAO(jdbcTemplate);
        this.staffDAO = new StaffDAO(jdbcTemplate);
        this.shelterDAO = new ShelterDAO(jdbcTemplate);
    }

    /**
     * Validates administrator credentials for sign-in.
     *
     * @param actualAdmin Administrator object with login details.
     * @return Admin object if authentication is successful, otherwise null.
     */
    public Admin signInLogic(Admin actualAdmin) {

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

    /**
     * Creates a new staff member record.
     *
     * @param staff Staff object containing details of the staff member to be created.
     * @return Staff object representing the created staff member, or null if creation fails.
     */
    public Staff createStaff(Staff staff) {

        if (staff == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Staff object can't be null.", 1);
            return null;
        }

        // store the hash of the password, not the password itself.
        staff.setPasswordHash(Hasher.hash(staff.getPasswordHash()));

        int createdStaffId = this.staffDAO.create(staff);
        if (createdStaffId < 1) {
            Logger.logMsgFrom(this.getClass().getName(), "Failed to create a staff record.", 1);
            return null;
        }

        Staff createdStaff = this.staffDAO.findById(createdStaffId);
        if (createdStaff != null) {
            Logger.logMsgFrom(this.getClass().getName(), "Created a staff record with id " + createdStaffId + " successfully.", 0);
            return createdStaff;
        }

        Logger.logMsgFrom(this.getClass().getName(), "Something had went wrong ..", 1);
        return null;
    }
    public Shelter createShelter(Shelter shelter) {
        if(shelter == null){
            Logger.logMsgFrom(this.getClass().getName(),"Shelter creation failed .. shelter object sent is null",1);
            return null;
        }
        int createdShelterId = this.shelterDAO.create(shelter);
        if(createdShelterId < 1){
            Logger.logMsgFrom(this.getClass().getName(),"Failed to create a shelter record",1);
            return null;
        }
        Shelter createdShelter = this.shelterDAO.findById(createdShelterId);
        if(createdShelter != null){
            Logger.logMsgFrom(this.getClass().getName(),"Created a shelter record with id "+createdShelterId + " successfully. ",0);
            return createdShelter;
        }
        Logger.logMsgFrom(this.getClass().getName(),"Something went wrong .. ",1);
        return null;
    }
    public List<Shelter> findAllShelters(){
        Logger.logMsgFrom(this.getClass().getName(),"Admin has requested to get all system shelters .. processing the request .. ",0);
        return this.shelterDAO.findAll();
    }
    public boolean deleteShelter(Shelter shelter){
        if(shelter == null ){
            Logger.logMsgFrom(this.getClass().getName(),"Shelter object to be deleted was sent null",1);
            return false;
        }
        Logger.logMsgFrom(this.getClass().getName(),"Admin has requested to deleter shelter with id: "+shelter.getId()+" processing the request .. ",0);
        return this.shelterDAO.delete(shelter);
    }
    public boolean deleteShelterByName(String name){
        Logger.logMsgFrom(this.getClass().getName(),"Admin has requested to deleter shelter with id: "+name+" processing the request .. ",0);
        return this.shelterDAO.deleteByName(name);
    }
}
