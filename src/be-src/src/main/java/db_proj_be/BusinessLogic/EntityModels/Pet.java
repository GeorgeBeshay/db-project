package db_proj_be.BusinessLogic.EntityModels;


import org.springframework.data.relational.core.sql.In;

import java.util.Objects;


public class Pet implements Identifiable {

    private int id;
    private String name;
    private String specie;
    private String breed;
    private String birthdate;
    private boolean gender;
    private String healthStatus;
    private String behaviour;
    private String description;
    private Integer shelterId;
    private boolean neutering;
    private boolean houseTraining;
    private boolean vaccination;

    public Pet() {}

    public Pet(String name, String specie, String breed, String birthdate,
               boolean gender, String healthStatus, String behaviour, String description,
               Integer shelterId, boolean neutering, boolean houseTraining, boolean vaccination) {
        this.name = name;
        this.specie = specie;
        this.breed = breed;
        this.birthdate = birthdate;
        this.gender = gender;
        this.healthStatus = healthStatus;
        this.behaviour = behaviour;
        this.description = description;
        this.shelterId = shelterId;
        this.neutering = neutering;
        this.houseTraining = houseTraining;
        this.vaccination = vaccination;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShelterId() {
        return shelterId;
    }

    public void setShelterId(Integer shelterId) {
        this.shelterId = shelterId;
    }

    public boolean isNeutering() {
        return neutering;
    }

    public void setNeutering(boolean neutering) {
        this.neutering = neutering;
    }

    public boolean isHouseTraining() {
        return houseTraining;
    }

    public void setHouseTraining(boolean houseTraining) {
        this.houseTraining = houseTraining;
    }

    public boolean isVaccination() {
        return vaccination;
    }

    public void setVaccination(boolean vaccination) {
        this.vaccination = vaccination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id &&
                gender == pet.gender &&
                shelterId == pet.shelterId &&
                neutering == pet.neutering &&
                houseTraining == pet.houseTraining &&
                vaccination == pet.vaccination &&
                Objects.equals(name, pet.name) &&
                Objects.equals(specie, pet.specie) &&
                Objects.equals(breed, pet.breed) &&
                Objects.equals(birthdate, pet.birthdate) &&
                Objects.equals(healthStatus, pet.healthStatus) &&
                Objects.equals(behaviour, pet.behaviour) &&
                Objects.equals(description, pet.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specie, breed, birthdate, gender, healthStatus, behaviour, description, shelterId, neutering, houseTraining, vaccination);
    }
}