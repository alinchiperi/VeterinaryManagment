package ro.usv.veterinarymanagment.DataModel;

import java.time.LocalDate;
import java.util.Date;

public class Animal {
    int id;
    String name;
    String birthDate;
    float weight;

    String species;
    int ownerId;

    public Animal(int id, String name, String birthDate, float weight, String species, int ownerId) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDare) {
        this.birthDate = birthDare;
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

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", weight=" + weight +
                ", species='" + species + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}
