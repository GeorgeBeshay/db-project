package db_proj_be.BusinessLogic.EntityModels;

import java.sql.Date;

public class ApplicationNotification {

    int appId;
    int adopterId;
    NotificationStatus status;
    Date date;

    public ApplicationNotification(int appId, int adopterId, NotificationStatus status, Date date) {
        this.appId = appId;
        this.adopterId = adopterId;
        this.status = status;
        this.date = date;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
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
