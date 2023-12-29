package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Services.StaffService;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("updateStaff")
    @ResponseBody
    public ResponseEntity<Staff> updateStaff(@RequestBody Staff staffToBeUpdated) {
        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to update an existing staff record .. " +
                "processing the request.", -1);
        Staff updatedStaff = this.staffService.updateStaff(staffToBeUpdated);
        return new ResponseEntity<>(updatedStaff, (updatedStaff != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
