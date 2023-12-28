package db_proj_be.BusinessLogic.EntityModels;

import java.sql.Date;
import java.util.Objects;

public class PetAvailabilityNotification {

    int petId;
    int adopterId;
    Boolean status; // Equivalent to isRead
    Date date;

    public PetAvailabilityNotification() {
    }

    public PetAvailabilityNotification(int petId, int adopterId, Boolean status, Date date) {
        this.petId = petId;
        this.adopterId = adopterId;
        this.status = status;
        this.date = date;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetAvailabilityNotification that = (PetAvailabilityNotification) o;
        return petId == that.petId && adopterId == that.adopterId && status.equals(that.status) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId, adopterId, status, date);
    }
}
