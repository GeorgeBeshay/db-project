package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.ApplicationNotification;
import db_proj_be.BusinessLogic.EntityModels.PetAvailabilityNotification;
import db_proj_be.BusinessLogic.Services.NotificationService;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@ComponentScan(basePackages = {"db_proj_be.BusinessLogic.Services", "db_proj_be.APIs"})
@RestController
@CrossOrigin()
@RequestMapping("/pasms-server/notification-api/")
public class NotificationAPI {

    private final NotificationService notificationService;

    @Autowired
    public NotificationAPI(JdbcTemplate jdbcTemplate) {
        this.notificationService = new NotificationService(jdbcTemplate);
    }

    @PostMapping("updateAppNot")
    @ResponseBody
    public ResponseEntity<Boolean> updateAppNotification(@RequestBody ApplicationNotification applicationNotification) {
        Logger.logMsgFrom(this.getClass().getName(), "An app notification requested to be updated.. ", -1);

        return (this.notificationService.updateAppNotification(applicationNotification))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("updatePetNot")
    @ResponseBody
    public ResponseEntity<Boolean> updatePetNotification(@RequestBody PetAvailabilityNotification petAvailabilityNotification) {
        Logger.logMsgFrom(this.getClass().getName(), "A pet requested to be updated.. ", -1);

        return (this.notificationService.updatePetNotification(petAvailabilityNotification))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}
