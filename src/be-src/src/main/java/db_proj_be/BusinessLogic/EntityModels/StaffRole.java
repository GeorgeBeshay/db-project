package db_proj_be.BusinessLogic.EntityModels;

public enum StaffRole {

    MANAGER("Manager"),
    MEMBER("Member");

    private final String value;

    StaffRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
