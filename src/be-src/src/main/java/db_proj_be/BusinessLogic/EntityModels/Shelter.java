package db_proj_be.BusinessLogic.EntityModels;

public class Shelter implements Identifiable {
    int id;
    String name;
    String location;
    String email;
    String phone;
    int manager;

    public Shelter(int id, String name, String location, String email, String phone, int manager) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.manager = manager;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getManager() {
        return manager;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "shelterId=" + id +
                ", shelterName='" + name + '\'' +
                ", shelterLocation='" + location + '\'' +
                ", shelterEmail='" + email + '\'' +
                ", shelterPhone='" + phone + '\'' +
                ", shelterManager=" + manager +
                '}';
    }
}
