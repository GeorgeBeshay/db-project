package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.Pet;
import db_proj_be.BusinessLogic.Services.VisitorService;
import db_proj_be.BusinessLogic.Utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@ComponentScan(basePackages = {"db_proj_be.BusinessLogic.Services", "db_proj_be.APIs"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/pasms-server/visitor-api/")

public class VisitorAPI {

    private final VisitorService visitorService;

    @Autowired
    public VisitorAPI(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.visitorService = new VisitorService(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @PostMapping("getSearchedAndSortedPets")
    @ResponseBody
    public ResponseEntity<List<Pet>> getSearchedAndSortedPets(@RequestBody Map<String, Object> requestedMap) {
        Logger.logMsgFrom(this.getClass().getName(), "Visitor requests for pets.. ", -1);

        List<Pet> pets = this.visitorService.getSearchedAndSortedPets(requestedMap);

        return new ResponseEntity<>(pets, (pets != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
