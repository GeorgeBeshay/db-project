package db_proj_be.BusinessLogic.EntityModels;

import db_proj_be.BusinessLogic.Utilities.Logger;

import java.util.Objects;

public class Staff implements Identifiable {
    int id;
    String firstName;
    String lastName;
    String role;
    String phone;
    String email;
    String passwordHash;
    int shelterId;

    public Staff(int id, String firstName, String lastName, String role, String phone, String email, String passwordHash, int shelterId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.shelterId = shelterId;
    }

    public Staff(){}

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (!role.equals(StaffRole.MANAGER.getValue()) && !role.equals(StaffRole.MEMBER.getValue())) {
            Logger.logMsgFrom(this.getClass().getName(), "Role is invalid.", 1);
            return;
        }

        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", paswordHash=" + passwordHash +
                ", shelterId=" + shelterId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return id == staff.id
                && Objects.equals(passwordHash, staff.passwordHash)
                && shelterId == staff.shelterId
                && Objects.equals(firstName, staff.firstName)
                && Objects.equals(lastName, staff.lastName)
                && Objects.equals(role, staff.role)
                && Objects.equals(phone, staff.phone)
                && Objects.equals(email, staff.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, role, phone, email, passwordHash, shelterId);
    }

}