package db_proj_be.APIs;

import db_proj_be.BusinessLogic.EntityModels.*;
import db_proj_be.BusinessLogic.Services.*;
import db_proj_be.BusinessLogic.Utilities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

@ComponentScan(basePackages = {"db_proj_be.BusinessLogic.Services", "db_proj_be.APIs"})
@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/pasms-server/pet-api/")

public class PetAPI {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PetService petService;

    @Autowired
    public PetAPI(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.petService = new PetService(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @PostMapping("createPet")
    @ResponseBody
    public ResponseEntity<Integer> createPet(@RequestBody Pet pet) {
        Logger.logMsgFrom(this.getClass().getName(), "A pet requested to be created.. ", -1);

        int petCreatedId = this.petService.createPet(pet);

        return (petCreatedId > 0)
                ? new ResponseEntity<>(petCreatedId, HttpStatus.OK)
                : new ResponseEntity<>(petCreatedId, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("updatePet")
    @ResponseBody
    public ResponseEntity<Boolean> updatePet(@RequestBody Pet pet) {
        Logger.logMsgFrom(this.getClass().getName(), "A pet requested to be updated.. ", -1);

        return (this.petService.updatePet(pet))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("deletePet")
    @ResponseBody
    public ResponseEntity<Boolean> deletePet(@RequestBody Pet pet) {
        Logger.logMsgFrom(this.getClass().getName(), "A pet requested to be deleted.. ", -1);

        return (this.petService.deletePet(pet))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

}
