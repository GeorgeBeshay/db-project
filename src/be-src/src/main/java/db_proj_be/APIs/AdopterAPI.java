package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.AdoptionApplication;
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
    public ResponseEntity<List<AdoptionApplication>> fetchAdoptionApplications(@PathVariable("id") int adopterId) {
        Logger.logMsgFrom(this.getClass().getName(), "Adoption applications requested by adopter " + adopterId, -1);

        List<AdoptionApplication> adoptionApplications = this.adopterService.fetchAdoptionApplications(adopterId);

        return (adoptionApplications != null)
                ? new ResponseEntity<>(adoptionApplications, HttpStatus.OK)
                : new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }
}