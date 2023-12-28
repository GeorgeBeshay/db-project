package db_proj_be.BusinessLogic.EntityModels;

import java.util.Objects;

public class Admin implements Identifiable {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String passwordHash;

    public Admin() {}

    public Admin(int id, String firstName, String lastName, String email, String phone, String passwordHash) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return id == admin.id &&
                Objects.equals(email, admin.email) &&
                Objects.equals(passwordHash, admin.passwordHash) &&
                Objects.equals(firstName, admin.firstName) &&
                Objects.equals(lastName, admin.lastName) &&
                Objects.equals(phone, admin.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, passwordHash, firstName, lastName, phone);
    }
}
