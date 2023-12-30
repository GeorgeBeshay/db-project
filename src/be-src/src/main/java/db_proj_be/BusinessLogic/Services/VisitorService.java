package db_proj_be.BusinessLogic.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import db_proj_be.BusinessLogic.EntityModels.*;
import db_proj_be.BusinessLogic.Utilities.Logger;
import db_proj_be.Database.DAOs.PetDAO;
import db_proj_be.Database.DAOs.ShelterDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class VisitorService {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PetDAO petDAO;
    private final ShelterDAO shelterDAO;

    public VisitorService(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        this.shelterDAO = new ShelterDAO(jdbcTemplate);
    }

    public List<Pet> getSearchedAndSortedPets(Map<String, Object> requestedMap) {
        Map<String, Object> criteria = (new ObjectMapper()).convertValue(requestedMap.get("criteria"), new TypeReference<Map<String, Object>>() {});
        String shelterLocation = (String) requestedMap.get("shelterLocation");
        List<String> orderedByColumns = (new ObjectMapper()).convertValue(requestedMap.get("orderedByColumns"), new TypeReference<List<String>>() {});

        return getPetsForVisitor(criteria, shelterLocation, orderedByColumns);
    }

    private List<Pet> getPetsForVisitor(Map<String, Object> criteria, String location, List<String> orderByColumns) {
        if (checkForOrderByColumns(orderByColumns) && checkCriteriaKeys(criteria)) {
            List<Shelter> shelters = null;
            if(location == null || location.length() == 0) {
                shelters = new ArrayList<>();
            }
            else {
                shelters = this.shelterDAO.findByLocation(location);
            }

            if(shelters != null) {
                List<Integer> shelterIds = new ArrayList<>();
                for(Shelter shelter : shelters) {
                    shelterIds.add(shelter.getId());
                }

                List<Pet> pets = this.petDAO.getPetsWithOptionsAndSorted(shelterIds, criteria, orderByColumns);
                if (pets != null) {
                    Logger.logMsgFrom(this.getClass().getName(), "Visitor get the pets successfully", 0);
                }
                return pets;
            }

            else {
                Logger.logMsgFrom(this.getClass().getName(), "Error in getting shelters from shelter location for search and filter", 1);
                return null;
            }
        }

        else {
            Logger.logMsgFrom(this.getClass().getName(), "Error in arguments for search and filter", 1);
            return null;
        }
    }

    private boolean checkForOrderByColumns(List<String> orderByColumns) {
        List<String> allowedColumns = Arrays.asList("neutering", "house_training", "vaccination");

        if (orderByColumns != null) {
            for (String column : orderByColumns) {
                if (!allowedColumns.contains(column)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkCriteriaKeys(Map<String, Object> criteria) {
        List<String> allowedKeys = Arrays.asList("specie", "breed", "birthdate", "gender");

        if (criteria != null) {
            for (String key : criteria.keySet()) {
                if (!allowedKeys.contains(key)) {
                    return false;
                }
            }
        }

        return true;
    }
}
