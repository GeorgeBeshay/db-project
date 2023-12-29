package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Staff;
import db_proj_be.BusinessLogic.Services.AdminService;
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

    @PostMapping("createStaff")
    @ResponseBody
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staffToBeCreated) {
        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to create a new staff record .. " +
                "processing the request.", -1);
        Staff createdStaff = this.adminService.createStaff(staffToBeCreated);
        return new ResponseEntity<>(createdStaff, (createdStaff != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
