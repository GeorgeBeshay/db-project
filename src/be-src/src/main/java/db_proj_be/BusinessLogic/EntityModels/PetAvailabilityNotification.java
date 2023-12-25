package db_proj_be.BusinessLogic.EntityModels;

import java.sql.Date;

public class PetAvailabilityNotification {

    int petId;
    int adopterId;
    NotificationStatus status;
    Date date;

    public PetAvailabilityNotification() {
    }

    public PetAvailabilityNotification(int petId, int adopterId, NotificationStatus status, Date date) {
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

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
