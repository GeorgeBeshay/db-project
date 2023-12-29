package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Shelter;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Services.AdminService;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ComponentScan(basePackages = {"db_proj_be.BusinessLogic.Services", "db_proj_be.APIs"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/pasms-server/admin-api/")
public class AdminAPI {

    private final AdminService adminService;

    @Autowired
    public AdminAPI(JdbcTemplate jdbcTemplate) {
        this.adminService = new AdminService(jdbcTemplate);
    }

    @PostMapping("adminSignIn")
    @ResponseBody
    public ResponseEntity<Admin> adminSignIn(@RequestBody Admin admin) {
        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to sign in .. " +
                "processing the request.", -1);

        Admin resultAdminObject = this.adminService.signInLogic(admin);
        return new ResponseEntity<>(resultAdminObject,
                (resultAdminObject != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
    @PostMapping("createShelter")
    @ResponseBody
    public ResponseEntity<Shelter> createShelter(@RequestBody Shelter shelterToBeCreated){
        Logger.logMsgFrom(this.getClass().getName(),"An Admin has requested to create a new shelter .."
        + "processing the request.",-1);

        Shelter createdShelter = this.adminService.createShelter(shelterToBeCreated);

        return new ResponseEntity<>(createdShelter,(createdShelter != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("createStaff")
    @ResponseBody
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staffToBeCreated) {
        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to create a new staff record .. " +
                "processing the request.", -1);
        Staff createdStaff = this.adminService.createStaff(staffToBeCreated);
        return new ResponseEntity<>(createdStaff, (createdStaff != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
    @PostMapping("deleteShelter")
    @ResponseBody
    public ResponseEntity<Boolean> deleteShelter(@RequestBody Shelter shelterToBeDeleted){
        Logger.logMsgFrom(this.getClass().getName(),"Admin has requested to delete a shelter.",0);
        boolean result = this.adminService.deleteShelter(shelterToBeDeleted);
        return new ResponseEntity<>(result,(result) ? HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }
    @PostMapping("findAllShelters")
    @ResponseBody
    public ResponseEntity<List<Shelter>> findAllShelters(){
        Logger.logMsgFrom(this.getClass().getName(),"Admin has requested to display all shelters.",0);
        List<Shelter> shelters = this.adminService.findAllShelters();
        return new ResponseEntity<>(shelters,(shelters != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
