package db_proj_be.BusinessLogic.EntityModels;

import java.util.Objects;

public class Shelter implements Identifiable {
    int id;
    String name;
    String location;
    String email;
    String phone;
    Integer manager;

    public Shelter(int id, String name, String location, String email, String phone, int manager) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.manager = manager;
    }

    public Shelter(){}

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

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return id == shelter.id &&
                Objects.equals(manager, shelter.manager) &&
                Objects.equals(name, shelter.name) &&
                Objects.equals(location, shelter.location) &&
                Objects.equals(email, shelter.email) &&
                Objects.equals(phone, shelter.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, email, phone, manager);
    }

}
