package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Admin;
import db_proj_be.BusinessLogic.EntityModels.Adopter;
import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
import db_proj_be.BusinessLogic.EntityModels.ApplicationNotification;
import db_proj_be.BusinessLogic.EntityModels.PetAvailabilityNotification;
import db_proj_be.BusinessLogic.Services.AdopterService;
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
@CrossOrigin()
@RequestMapping("/pasms-server/adopter-api/")
public class AdopterAPI {

    private final AdopterService adopterService;

    @Autowired
    public AdopterAPI(JdbcTemplate jdbcTemplate) {
        this.adopterService = new AdopterService(jdbcTemplate);
    }

    @PostMapping("submit-application")
    public ResponseEntity<Integer> submitAdoptionApplication(@RequestBody AdoptionApplication adoptionApplication) {
        Logger.logMsgFrom(this.getClass().getName(), "An adoption application is requested to be submitted.. ", -1);

        int adoptionAppId = this.adopterService.createAdoptionApplication(adoptionApplication);

        return (adoptionAppId > 0)
                ? new ResponseEntity<>(adoptionAppId, HttpStatus.OK)
                : new ResponseEntity<>(adoptionAppId, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("fetch-applications/{id}")
    @ResponseBody
    public ResponseEntity<List<AdoptionApplication>> fetchAdoptionApplications(@PathVariable("id") int adopterId) {
        Logger.logMsgFrom(this.getClass().getName(), "Adoption applications requested by adopter " + adopterId, -1);

        List<AdoptionApplication> adoptionApplications = this.adopterService.fetchAdoptionApplications(adopterId);

        return (adoptionApplications != null)
                ? new ResponseEntity<>(adoptionApplications, HttpStatus.OK)
                : new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("get-by-id/{id}")
    @ResponseBody
    public ResponseEntity<Adopter> findById(@PathVariable("id") int adopterId) {
        Logger.logMsgFrom(this.getClass().getName(), "Adopter requested with id " + adopterId, -1);

        Adopter adopter = this.adopterService.findById(adopterId);

        return (adopter != null)
                ? new ResponseEntity<>(adopter, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("sign-in")
    @ResponseBody
    public ResponseEntity<Adopter> signIn(@RequestBody Adopter adopter) {
        Logger.logMsgFrom(this.getClass().getName(), "An adopter has requested to sign in .. " +
                "processing the request.", -1);

        Adopter resultAdopterObject = this.adopterService.signInLogic(adopter);
        return new ResponseEntity<>(resultAdopterObject,
                (resultAdopterObject != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("sign-up")
    @ResponseBody
    public ResponseEntity<Adopter> signUp(@RequestBody Adopter adopter) {
        Logger.logMsgFrom(this.getClass().getName(), "An adopter has requested to sign up .. " +
                "processing the request.", -1);

        Adopter resultAdopterObject = this.adopterService.signUpLogic(adopter);
        return new ResponseEntity<>(resultAdopterObject,
                (resultAdopterObject != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("fetch-app-notifications/{id}")
    @ResponseBody
    public ResponseEntity<List<ApplicationNotification>> fetchAppNotifications(@PathVariable("id") int adopterId) {
        Logger.logMsgFrom(this.getClass().getName(), "App notifications requested by adopter " + adopterId, -1);

        List<ApplicationNotification> applicationNotifications = this.adopterService.fetchAppNotifications(adopterId);

        return (applicationNotifications != null)
                ? new ResponseEntity<>(applicationNotifications, HttpStatus.OK)
                : new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("fetch-pet-notifications/{id}")
    @ResponseBody
    public ResponseEntity<List<PetAvailabilityNotification>> fetchPetNotifications(@PathVariable("id") int adopterId) {
        Logger.logMsgFrom(this.getClass().getName(), "Pet notifications requested by adopter " + adopterId, -1);

        List<PetAvailabilityNotification> petNotifications = this.adopterService.fetchPetNotifications(adopterId);

        return (petNotifications != null)
                ? new ResponseEntity<>(petNotifications, HttpStatus.OK)
                : new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

}
