package db_proj_be.BusinessLogic.EntityModels;

import java.util.Objects;

public class Adoption {

    int petId;
    int adopterId;

    public Adoption() {
    }

    public Adoption(int petId, int adopterId) {
        this.petId = petId;
        this.adopterId = adopterId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adoption adoption = (Adoption) o;
        return petId == adoption.getPetId() && adopterId == adoption.getAdopterId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId, adopterId);
    }

}
