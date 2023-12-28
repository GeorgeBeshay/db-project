package db_proj_be.BusinessLogic.EntityModels;

import java.util.Objects;

public class AdoptionApplication implements Identifiable {

    int id;
    int adopterId;
    int petId;
    ApplicationStatus status;
    String description;
    Boolean experience;
    String creationDate;
    String closingDate;

    public AdoptionApplication() {
    }

    public AdoptionApplication(int adopterId, int petId, ApplicationStatus status, String description, Boolean experience, String creationDate, String closingDate) {
        this.adopterId = adopterId;
        this.petId = petId;
        this.status = status;
        this.description = description;
        this.experience = experience;
        this.creationDate = creationDate;
        this.closingDate = closingDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getExperience() {
        return experience;
    }

    public void setExperience(Boolean experience) {
        this.experience = experience;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdoptionApplication that = (AdoptionApplication) o;
        return adopterId == that.adopterId && petId == that.petId && status == that.status && description.equals(that.description) && experience.equals(that.experience) && creationDate.equals(that.creationDate) && Objects.equals(closingDate, that.closingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, adopterId, petId, status, description, experience, creationDate, closingDate);
    }
}
