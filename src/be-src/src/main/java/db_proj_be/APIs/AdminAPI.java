package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.Services.AdminServices;
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

    private final JdbcTemplate jdbcTemplate;
    private final AdminServices adminServices;

    @Autowired
    public AdminAPI(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.adminServices = new AdminServices(jdbcTemplate);
    }

    @PostMapping("adminSignIn")
    @ResponseBody
    public ResponseEntity<Admin> adminSignIn(@RequestBody Admin admin) {
        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to sign in .. " +
                "processing the request.", -1);

        Admin resultAdminObject = this.adminServices.SignInLogic(admin);

        return new ResponseEntity<>(resultAdminObject,
                (resultAdminObject != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
