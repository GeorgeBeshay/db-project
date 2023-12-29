package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.AdminDAO;
import db_proj_be.Database.DAOs.ShelterDAO;
import db_proj_be.Database.DAOs.StaffDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;

public class AdminServices {
    private final JdbcTemplate jdbcTemplate;
    private final AdminDAO adminDAO;
    private final StaffDAO staffDAO;
    //TODO import ShelterDAO class
    private final ShelterDAO shelterDAO;

    public AdminServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.adminDAO = new AdminDAO(jdbcTemplate);
    }

    public Admin adminSignInLogic(Admin actualAdmin) {

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

    public boolean createShelter(Shelter shelter) {
        if(shelter == null){
            Logger.logMsgFrom(this.getClass().getName(),"Shelter creation failed .. shelter object sent is null",1);
            return false;
        }
        try {
            //TODO: createShelter() to return a boolean value
            return this.shelterDAO.createShelter(shelter)
        }
    }
    public boolean updateStaffShelter(int shelterId){
        Logger.logMsgFrom(this.getClass().getName(),);
        return this.staffDAO.updateStaffShelter(shelterId);
    }
}
