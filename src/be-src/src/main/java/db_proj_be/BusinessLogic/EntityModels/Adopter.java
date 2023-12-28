package db_proj_be.BusinessLogic.EntityModels;

import java.util.Objects;

public class Adopter implements Identifiable {

    private int id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phone;
    private String birthDate;
    private boolean gender; // equivalent to isMale
    private String address;

    public Adopter() {}

    public Adopter(int id, String email, String passwordHash, String firstName, String lastName, String phone, String birthDate, boolean gender, String address) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopter adopter = (Adopter) o;
        return id == adopter.id &&
                gender == adopter.gender &&
                Objects.equals(email, adopter.email) &&
                Objects.equals(passwordHash, adopter.passwordHash) &&
                Objects.equals(firstName, adopter.firstName) &&
                Objects.equals(lastName, adopter.lastName) &&
                Objects.equals(phone, adopter.phone) &&
                Objects.equals(birthDate, adopter.birthDate) &&
                Objects.equals(address, adopter.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, passwordHash, firstName, lastName, phone, birthDate, gender, address);
    }
}
