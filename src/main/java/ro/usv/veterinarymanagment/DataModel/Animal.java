package ro.usv.veterinarymanagment.DataModel;

import java.util.Date;

public class Animal {
    int id;
    String name;
    Date birthDare;
    float weight;
    String species;
    int ownerId;

    public Animal(int id, String name, Date birthDare, float weight, String species, int ownerId) {
        this.id = id;
        this.name = name;
        this.birthDare = birthDare;
        this.weight = weight;
        this.species = species;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDare() {
        return birthDare;
    }

    public void setBirthDare(Date birthDare) {
        this.birthDare = birthDare;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
