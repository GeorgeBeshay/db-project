package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
import db_proj_be.BusinessLogic.EntityModels.ApplicationStatus;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Services.StaffService;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@ComponentScan(basePackages = {"db_proj_be.BusinessLogic.Services", "db_proj_be.APIs"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/pasms-server/staff-api/")
public class StaffAPI {

    private final StaffService staffService;

    @Autowired
    public StaffAPI(JdbcTemplate jdbcTemplate) {
        this.staffService = new StaffService(jdbcTemplate);
    }

    @GetMapping("fetch-applications")
    public ResponseEntity<List<AdoptionApplication>> fetchAllAdoptionApplications() {
        Logger.logMsgFrom(this.getClass().getName(), "Adoption applications requested by staff..", -1);

        List<AdoptionApplication> adoptionApplications = this.staffService.fetchAllAdoptionApplications();

        return (adoptionApplications != null)
                ? new ResponseEntity<>(adoptionApplications, HttpStatus.OK)
                : new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("fetch-applications-by-status")
    public ResponseEntity<List<AdoptionApplication>> fetchAdoptionApplicationsByStatus(@RequestParam("status") ApplicationStatus status) {
        Logger.logMsgFrom(this.getClass().getName(), status + " adoption applications requested by staff..", -1);

        List<AdoptionApplication> adoptionApplications = this.staffService.fetchAdoptionApplicationsByStatus(status);

        return (adoptionApplications != null)
                ? new ResponseEntity<>(adoptionApplications, HttpStatus.OK)
                : new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("update-application")
    public ResponseEntity<Boolean> updateApplication(@RequestBody AdoptionApplication adoptionApplication) {
        Logger.logMsgFrom(this.getClass().getName(), "An adoption application requested to be updated.. ", -1);

        return (this.staffService.updateApplication(adoptionApplication))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
  
    @PostMapping("updateStaff")
    @ResponseBody
    public ResponseEntity<Staff> updateStaff(@RequestBody Staff staffToBeUpdated) {
        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to update an existing staff record .. " +
                "processing the request.", -1);
        Staff updatedStaff = this.staffService.updateStaff(staffToBeUpdated);
        return new ResponseEntity<>(updatedStaff, (updatedStaff != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
