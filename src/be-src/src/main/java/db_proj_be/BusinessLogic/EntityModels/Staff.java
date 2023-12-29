package db_proj_be.BusinessLogic.EntityModels;

public class Staff implements Identifiable {
    int id;
    String firstName;
    String lastName;
    String role;
    String phone;
    String email;
    int paswordHash;
    int shelterId;

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

    public int getPaswordHash() {
        return paswordHash;
    }

    public void setPaswordHash(int paswordHash) {
        this.paswordHash = paswordHash;
    }

    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
    }

    public Staff(int id, String firstName, String lastName, String role, String phone, String email, int paswordHash, int shelterId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.paswordHash = paswordHash;
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
                ", paswordHash=" + paswordHash +
                ", shelterId=" + shelterId +
                '}';
    }
}
