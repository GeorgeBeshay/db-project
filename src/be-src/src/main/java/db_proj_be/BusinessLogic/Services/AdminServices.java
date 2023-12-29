package db_proj_be.BusinessLogic.Services;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.AdminDAO;
import db_proj_be.Database.DAOs.ShelterDAO;
import db_proj_be.Database.DAOs.StaffDAO;
import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public class AdminServices {
    private final JdbcTemplate jdbcTemplate;
    private final AdminDAO adminDAO;
    private final StaffDAO staffDAO;
    //TODO import ShelterDAO class
    private final ShelterDAO shelterDAO;

    public AdminServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.adminDAO = new AdminDAO(jdbcTemplate);
        this.shelterDAO = new ShelterDAO(jdbcTemplate);
        this.staffDAO = new StaffDAO(jdbcTemplate);
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
            Logger.logMsgFrom(this.getClass().getName(), "Admin has requested to create a new shelter .. processing the request",0);
            return this.shelterDAO.create(shelter) != -1;
        }catch (Exception e){
            Logger.logMsgFrom(this.getClass().getName(),"Shelter creation failed"+e.getMessage(),1);
            return false;
        }
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
