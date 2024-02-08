package db_proj_be.APIs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import db_proj_be.BusinessLogic.EntityModels.*;
import db_proj_be.BusinessLogic.Utilities.Hasher;
import db_proj_be.Database.DAOs.*;
import db_proj_be.besrc.BeSrcApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeSrcApplication.class)
@AutoConfigureMockMvc
public class StaffAPITests {

    private final MockMvc mockMvc;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StaffAPITests(MockMvc mockMvc, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // Helper method to convert Map to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------- Tests ----------------

    @Test
    @DisplayName("Staff Updating his / her profile - Successful")
    public void testUpdateStaffSuccessful() throws Exception {
        // Arrange

        // creating shelter
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // prepare a staff object
        int staffId = -1; // will be ignored.
        String firstName = "John";
        String lastName = "Doe";
        StaffRole role = StaffRole.MANAGER;
        String email = "johnDoe@gmail.com";
        String passwordHash = "This is the password hash";

        Staff staff = new Staff();
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setRole(role.getValue());
        staff.setEmail(email);
        staff.setPasswordHash(passwordHash);
        staff.setShelterId(shelterId);

        StaffDAO staffDAO = new StaffDAO(jdbcTemplate);
        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // modify the record
        StaffRole newRole = StaffRole.MEMBER;
        String modifiedEmail = "johnDoeModified@gmail.com";
        staff.setRole(newRole.getValue());
        staff.setEmail(modifiedEmail);


        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/updateStaff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content as an Admin object
        Staff resultStaff = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Staff.class);

        // Assert the status code and Admin object
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(resultStaff);
        assertEquals(staff, resultStaff);

        Staff retrievedStaffFromDB = staffDAO.findById(staffId);
        assertEquals(staff, retrievedStaffFromDB);
        assertEquals(resultStaff, retrievedStaffFromDB);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Updating his / her profile - Failure")
    public void testUpdateStaffFailure() throws Exception {
        // Arrange

        // creating shelter
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // prepare a staff object
        int staffId = -1; // will be ignored.
        String firstName = "John";
        String lastName = "Doe";
        StaffRole role = StaffRole.MANAGER;
        String email = "johnDoe@gmail.com";
        String passwordHash = "This is the password hash";

        Staff staff = new Staff();
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setRole(role.getValue());
        staff.setEmail(email);
        staff.setPasswordHash(passwordHash);
        staff.setShelterId(shelterId);

        StaffDAO staffDAO = new StaffDAO(jdbcTemplate);
        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // modify the record
        String newRole = "INVALID ROLE";
        staff.setRole(newRole);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/updateStaff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        staff.setRole(role.getValue()); // reset the object to its initial state before modification.
        Staff retrievedStaffFromDB = staffDAO.findById(staffId);
        assertEquals(staff, retrievedStaffFromDB);

        // try to modify another attribute with some invalid value
        int newShelterId = -1;
        staff.setShelterId(newShelterId);

        // Act again
        result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/updateStaff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        status = result.getResponse().getStatus();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        staff.setShelterId(shelterId); // reset the object to its initial state before modification.
        retrievedStaffFromDB = staffDAO.findById(staffId);
        assertEquals(staff, retrievedStaffFromDB);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Sign in - Successful")
    public void testSignInSuccessful() throws Exception {
        // Arrange

        // creating shelter
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // prepare a staff object
        int staffId = -1; // will be ignored.
        String firstName = "John";
        String lastName = "Doe";
        StaffRole role = StaffRole.MANAGER;
        String email = "johnDoe@gmail.com";
        String passwordHash = "This is the password hash";

        Staff staff = new Staff();
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setRole(role.getValue());
        staff.setEmail(email);
        staff.setPasswordHash(Hasher.hash(passwordHash));
        staff.setShelterId(shelterId);

        StaffDAO staffDAO = new StaffDAO(jdbcTemplate);
        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // use the original password
        staff.setPasswordHash(passwordHash);

        // suppose that the ID is unknown
        staff.setId(-1);


        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content as an Admin object
        Staff resultStaff = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Staff.class);

        // Assert the status code and Admin object
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(resultStaff);
        staff.setPasswordHash(Hasher.hash(passwordHash));
        staff.setId(staffId);
        assertEquals(staff, resultStaff);

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Sign in - Failure")
    public void testSignInFailure() throws Exception {
        // Arrange

        // creating shelter
        int shelterId;
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // prepare a staff object
        int staffId = -1; // will be ignored.
        String firstName = "John";
        String lastName = "Doe";
        StaffRole role = StaffRole.MANAGER;
        String email = "johnDoe@gmail.com";
        String passwordHash = "This is the password hash";

        Staff staff = new Staff();
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setRole(role.getValue());
        staff.setEmail(email);
        staff.setPasswordHash(Hasher.hash(passwordHash));
        staff.setShelterId(shelterId);

        StaffDAO staffDAO = new StaffDAO(jdbcTemplate);
        staffId = staffDAO.create(staff);
        assertTrue(staffId >= 1);
        staff.setId(staffId);

        // use the original password
        staff.setPasswordHash(passwordHash + "modified");

        // suppose that the ID is unknown
        staff.setId(-1);


        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8081/pasms-server/staff-api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(staff)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Assert the status code and Admin object
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM STAFF WHERE id = ?", staffId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Fetching All Adoption Applications")
    public void testFetchAllAdoptionApplications() throws Exception {
        // Arrange
        int adoptionApplicationId;
        int adoptionApplicationId2;
        int adopterId;
        int petId;
        int petId2;
        int shelterId;

        // Create a shelter
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // Create an adopter
        String adopterEmail = "TESTADOPTER@mail.com";
        String passwordHash = "hashedPassword";
        String firstName = "TEST";
        String lastName = "ADOPTER";
        String address = "addressXYZ";

        Adopter adopter = new Adopter();
        adopter.setEmail(adopterEmail);
        adopter.setPasswordHash(passwordHash);
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setAddress(address);

        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        adopterId = adopterDAO.create(adopter);
        assertTrue(adopterId >= 1);
        adopter.setId(adopterId);

        // Create a pet
        String name = "testPet";
        String specie = "testSpecie";
        String breed = "testBreed";
        boolean gender = true;
        String healthStatus = "testHealthStatus";

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecie(specie);
        pet.setBreed(breed);
        pet.setGender(gender);
        pet.setHealthStatus(healthStatus);
        pet.setShelterId(shelterId);

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId = petDAO.create(pet);
        assertTrue(petId >= 1);
        pet.setId(petId);

        // Create a second pet
        String name2 = "testPet";
        String specie2 = "testSpecie";
        String breed2 = "testBreed";
        boolean gender2 = true;
        String healthStatus2 = "testHealthStatus";

        Pet pet2 = new Pet();
        pet2.setName(name2);
        pet2.setSpecie(specie2);
        pet2.setBreed(breed2);
        pet2.setGender(gender2);
        pet2.setHealthStatus(healthStatus2);
        pet2.setShelterId(shelterId);

        petId2 = petDAO.create(pet2);
        assertTrue(petId2 >= 1);
        pet2.setId(petId2);

        // Create an adoption application
        ApplicationStatus applicationStatus = ApplicationStatus.PENDING;
        Boolean experience = true;
        String creationDate = "2018-05-05";
        String description = "testDescription";

        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setAdopterId(adopterId);
        adoptionApplication.setPetId(petId);
        adoptionApplication.setStatus(applicationStatus);
        adoptionApplication.setExperience(experience);
        adoptionApplication.setCreationDate(creationDate);
        adoptionApplication.setDescription(description);

        AdoptionApplicationDAO adoptionApplicationDAO = new AdoptionApplicationDAO(jdbcTemplate);
        adoptionApplicationId = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(adoptionApplicationId >= 1);
        adoptionApplication.setId(adoptionApplicationId);

        // Create a second adoption application
        ApplicationStatus applicationStatus2 = ApplicationStatus.PENDING;
        Boolean experience2 = true;
        String creationDate2 = "2018-10-10";
        String description2 = "testDescription";

        AdoptionApplication adoptionApplication2 = new AdoptionApplication();
        adoptionApplication2.setAdopterId(adopterId);
        adoptionApplication2.setPetId(petId2);
        adoptionApplication2.setStatus(applicationStatus2);
        adoptionApplication2.setExperience(experience2);
        adoptionApplication2.setCreationDate(creationDate2);
        adoptionApplication2.setDescription(description2);

        adoptionApplicationId2 = adoptionApplicationDAO.create(adoptionApplication2);
        assertTrue(adoptionApplicationId2 >= 1);
        adoptionApplication2.setId(adoptionApplicationId2);

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8081/pasms-server/staff-api/fetch-applications")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content as a list of adoption applications
        List<AdoptionApplication> adoptionApplications = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<AdoptionApplication>>() {});

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(adoptionApplications);
        assertTrue(adoptionApplications.size() >= 2);
        assertTrue(adoptionApplications.contains(adoptionApplication));
        assertTrue(adoptionApplications.contains(adoptionApplication2));

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", adoptionApplicationId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", adoptionApplicationId2));
        assertEquals(1, jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2));
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Fetching Adoption Applications by Status")
    public void testFetchAdoptionApplicationsByStatus() throws Exception {
        // Arrange
        int adoptionApplicationId;
        int adoptionApplicationId2;
        int adopterId;
        int petId;
        int petId2;
        int shelterId;

        // Create a shelter
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // Create an adopter
        String adopterEmail = "TESTADOPTER@mail.com";
        String passwordHash = "hashedPassword";
        String firstName = "TEST";
        String lastName = "ADOPTER";
        String address = "addressXYZ";

        Adopter adopter = new Adopter();
        adopter.setEmail(adopterEmail);
        adopter.setPasswordHash(passwordHash);
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setAddress(address);

        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        adopterId = adopterDAO.create(adopter);
        assertTrue(adopterId >= 1);
        adopter.setId(adopterId);

        // Create a pet
        String name = "testPet";
        String specie = "testSpecie";
        String breed = "testBreed";
        boolean gender = true;
        String healthStatus = "testHealthStatus";

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecie(specie);
        pet.setBreed(breed);
        pet.setGender(gender);
        pet.setHealthStatus(healthStatus);
        pet.setShelterId(shelterId);

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId = petDAO.create(pet);
        assertTrue(petId >= 1);
        pet.setId(petId);

        // Create a second pet
        String name2 = "testPet";
        String specie2 = "testSpecie";
        String breed2 = "testBreed";
        boolean gender2 = true;
        String healthStatus2 = "testHealthStatus";

        Pet pet2 = new Pet();
        pet2.setName(name2);
        pet2.setSpecie(specie2);
        pet2.setBreed(breed2);
        pet2.setGender(gender2);
        pet2.setHealthStatus(healthStatus2);
        pet2.setShelterId(shelterId);

        petId2 = petDAO.create(pet2);
        assertTrue(petId2 >= 1);
        pet2.setId(petId2);

        // Create an adoption application
        ApplicationStatus applicationStatus = ApplicationStatus.PENDING;
        Boolean experience = true;
        String creationDate = "2018-05-05";
        String description = "testDescription";

        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setAdopterId(adopterId);
        adoptionApplication.setPetId(petId);
        adoptionApplication.setStatus(applicationStatus);
        adoptionApplication.setExperience(experience);
        adoptionApplication.setCreationDate(creationDate);
        adoptionApplication.setDescription(description);

        AdoptionApplicationDAO adoptionApplicationDAO = new AdoptionApplicationDAO(jdbcTemplate);
        adoptionApplicationId = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(adoptionApplicationId >= 1);
        adoptionApplication.setId(adoptionApplicationId);

        // Create a second adoption application
        ApplicationStatus applicationStatus2 = ApplicationStatus.APPROVED;
        Boolean experience2 = true;
        String creationDate2 = "2018-10-10";
        String description2 = "testDescription";

        AdoptionApplication adoptionApplication2 = new AdoptionApplication();
        adoptionApplication2.setAdopterId(adopterId);
        adoptionApplication2.setPetId(petId2);
        adoptionApplication2.setStatus(applicationStatus2);
        adoptionApplication2.setExperience(experience2);
        adoptionApplication2.setCreationDate(creationDate2);
        adoptionApplication2.setDescription(description2);

        adoptionApplicationId2 = adoptionApplicationDAO.create(adoptionApplication2);
        assertTrue(adoptionApplicationId2 >= 1);
        adoptionApplication2.setId(adoptionApplicationId2);

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8081/pasms-server/staff-api/fetch-applications-by-status").param("status", ApplicationStatus.PENDING.getValue())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content as a list of adoption applications
        List<AdoptionApplication> adoptionApplications = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<AdoptionApplication>>() {});

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(adoptionApplications);
        assertFalse(adoptionApplications.isEmpty());
        assertTrue(adoptionApplications.contains(adoptionApplication));
        assertFalse(adoptionApplications.contains(adoptionApplication2));

        // Act
        result = mockMvc.perform(get("http://localhost:8081/pasms-server/staff-api/fetch-applications-by-status").param("status", ApplicationStatus.APPROVED.getValue())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Retrieve the response status code
        status = result.getResponse().getStatus();

        // Retrieve the response content as a list of adoption applications
        adoptionApplications = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<AdoptionApplication>>() {});

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(adoptionApplications);
        assertFalse(adoptionApplications.isEmpty());
        assertFalse(adoptionApplications.contains(adoptionApplication));
        assertTrue(adoptionApplications.contains(adoptionApplication2));

        // Clean
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", adoptionApplicationId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", adoptionApplicationId2));
        assertEquals(1, jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2));
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Updating Adoption Applications")
    public void testUpdateApplication() throws Exception {
        // Arrange
        int adoptionApplicationId;
        int adoptionApplicationId2;
        int adopterId;
        int petId;
        int petId2;
        int shelterId;

        // Create a shelter
        String shelterName = "SHELTER X";
        String shelterLocation = "SHELTER X LOCATION";
        String shelterPhone = "123456789";
        String shelterEmail = "shelterX@gmail.com";

        Shelter shelter = new Shelter();
        shelter.setName(shelterName);
        shelter.setLocation(shelterLocation);
        shelter.setPhone(shelterPhone);
        shelter.setEmail(shelterEmail);

        ShelterDAO shelterDAO = new ShelterDAO(jdbcTemplate);
        shelterId = shelterDAO.create(shelter);
        assertTrue(shelterId >= 1);
        shelter.setId(shelterId);

        // Create an adopter
        String adopterEmail = "TESTADOPTER@mail.com";
        String passwordHash = "hashedPassword";
        String firstName = "TEST";
        String lastName = "ADOPTER";
        String address = "addressXYZ";

        Adopter adopter = new Adopter();
        adopter.setEmail(adopterEmail);
        adopter.setPasswordHash(passwordHash);
        adopter.setFirstName(firstName);
        adopter.setLastName(lastName);
        adopter.setAddress(address);

        AdopterDAO adopterDAO = new AdopterDAO(jdbcTemplate);
        adopterId = adopterDAO.create(adopter);
        assertTrue(adopterId >= 1);
        adopter.setId(adopterId);

        // Create a pet
        String name = "testPet";
        String specie = "testSpecie";
        String breed = "testBreed";
        boolean gender = true;
        String healthStatus = "testHealthStatus";

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecie(specie);
        pet.setBreed(breed);
        pet.setGender(gender);
        pet.setHealthStatus(healthStatus);
        pet.setShelterId(shelterId);

        PetDAO petDAO = new PetDAO(jdbcTemplate, namedParameterJdbcTemplate);
        petId = petDAO.create(pet);
        assertTrue(petId >= 1);
        pet.setId(petId);

        // Create a second pet
        String name2 = "testPet";
        String specie2 = "testSpecie";
        String breed2 = "testBreed";
        boolean gender2 = true;
        String healthStatus2 = "testHealthStatus";

        Pet pet2 = new Pet();
        pet2.setName(name2);
        pet2.setSpecie(specie2);
        pet2.setBreed(breed2);
        pet2.setGender(gender2);
        pet2.setHealthStatus(healthStatus2);
        pet2.setShelterId(shelterId);

        petId2 = petDAO.create(pet2);
        assertTrue(petId2 >= 1);
        pet2.setId(petId2);

        // Create an adoption application
        ApplicationStatus applicationStatus = ApplicationStatus.PENDING;
        Boolean experience = true;
        String creationDate = "2018-05-05";
        String description = "testDescription";

        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setAdopterId(adopterId);
        adoptionApplication.setPetId(petId);
        adoptionApplication.setStatus(applicationStatus);
        adoptionApplication.setExperience(experience);
        adoptionApplication.setCreationDate(creationDate);
        adoptionApplication.setDescription(description);

        AdoptionApplicationDAO adoptionApplicationDAO = new AdoptionApplicationDAO(jdbcTemplate);
        adoptionApplicationId = adoptionApplicationDAO.create(adoptionApplication);
        assertTrue(adoptionApplicationId >= 1);
        adoptionApplication.setId(adoptionApplicationId);

        // Create a second adoption application
        ApplicationStatus applicationStatus2 = ApplicationStatus.APPROVED;
        Boolean experience2 = true;
        String creationDate2 = "2018-10-10";
        String description2 = "testDescription";

        AdoptionApplication adoptionApplication2 = new AdoptionApplication();
        adoptionApplication2.setAdopterId(adopterId);
        adoptionApplication2.setPetId(petId2);
        adoptionApplication2.setStatus(applicationStatus2);
        adoptionApplication2.setExperience(experience2);
        adoptionApplication2.setCreationDate(creationDate2);
        adoptionApplication2.setDescription(description2);

        adoptionApplicationId2 = adoptionApplicationDAO.create(adoptionApplication2);
        assertTrue(adoptionApplicationId2 >= 1);
        adoptionApplication2.setId(adoptionApplicationId2);

        // Update application
        adoptionApplication.setStatus(ApplicationStatus.APPROVED);
        adoptionApplication.setClosingDate("2018-07-07");

        MvcResult result = mockMvc.perform(put("http://localhost:8081/pasms-server/staff-api/update-application")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(adoptionApplication))
        ).andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        Boolean updateResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Boolean.class);

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(updateResult);

        // Update the second application
        adoptionApplication2.setStatus(ApplicationStatus.PENDING);
        adoptionApplication2.setClosingDate("2018-11-11");

        result = mockMvc.perform(put("http://localhost:8081/pasms-server/staff-api/update-application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(adoptionApplication2))
        ).andReturn();

        // Retrieve the response status code
        status = result.getResponse().getStatus();

        updateResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Boolean.class);

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(updateResult);

        // Fetch adoption applications and check for  the new status values
        result = mockMvc.perform(get("http://localhost:8081/pasms-server/staff-api/fetch-applications-by-status").param("status", ApplicationStatus.PENDING.getValue())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Retrieve the response status code
        status = result.getResponse().getStatus();

        // Retrieve the response content as a list of adoption applications
        List<AdoptionApplication> adoptionApplications = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<AdoptionApplication>>() {});

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(adoptionApplications);
        assertFalse(adoptionApplications.isEmpty());
        assertTrue(adoptionApplications.contains(adoptionApplication2));
        assertFalse(adoptionApplications.contains(adoptionApplication));

        // Fetch adoption applications and check for  the new status values
        result = mockMvc.perform(get("http://localhost:8081/pasms-server/staff-api/fetch-applications-by-status").param("status", ApplicationStatus.APPROVED.getValue())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Retrieve the response status code
        status = result.getResponse().getStatus();

        // Retrieve the response content as a list of adoption applications
        adoptionApplications = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<AdoptionApplication>>() {});

        // Assert
        assertEquals(HttpStatus.OK.value(), status);
        assertNotNull(adoptionApplications);
        assertFalse(adoptionApplications.isEmpty());
        assertFalse(adoptionApplications.contains(adoptionApplication2));
        assertTrue(adoptionApplications.contains(adoptionApplication));

        // Clean
        assertEquals(2, jdbcTemplate.update("DELETE FROM APPLICATION_NOTIFICATION WHERE adopter_id = ?", adopterId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTION WHERE adopter_id = ?", adopterId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", adoptionApplicationId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTION_APPLICATION WHERE id = ?", adoptionApplicationId2));
        assertEquals(1, jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM PET WHERE id = ?", petId2));
        assertEquals(1, jdbcTemplate.update("DELETE FROM ADOPTER WHERE id = ?", adopterId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM SHELTER WHERE id = ?", shelterId));
    }

    @Test
    @DisplayName("Staff Updating Adoption Application Failure")
    public void testUpdateApplicationFailure() throws Exception {
        // Arrange
        int adoptionApplicationId = -1;
        AdoptionApplication adoptionApplication = new AdoptionApplication();
        adoptionApplication.setId(adoptionApplicationId);

        // Act
        MvcResult result = mockMvc.perform(put("http://localhost:8081/pasms-server/staff-api/update-application")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(adoptionApplication))
        ).andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();
        Boolean updateResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Boolean.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(updateResult);
    }

}
