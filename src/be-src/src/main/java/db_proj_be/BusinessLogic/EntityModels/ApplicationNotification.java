package db_proj_be.BusinessLogic.EntityModels;

import java.sql.Date;
import java.util.Objects;

public class ApplicationNotification {

    int applicationId;
    int adopterId;
    Boolean status; // Equivalent to isRead
    Date date;

    public ApplicationNotification() {
    }

    public ApplicationNotification(int applicationId, int adopterId, Boolean status, Date date) {
        this.applicationId = applicationId;
        this.adopterId = adopterId;
        this.status = status;
        this.date = date;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
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
        ApplicationNotification that = (ApplicationNotification) o;
        return applicationId == that.applicationId && adopterId == that.adopterId && status.equals(that.status) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, adopterId, status, date);
    }
}
