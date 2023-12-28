package db_proj_be.BusinessLogic.EntityModels;

public enum NotificationStatus {
    UNREAD("UNREAD"),
    READ("READ");

    private final String value;

    NotificationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
