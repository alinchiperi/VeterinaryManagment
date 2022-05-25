package ro.usv.veterinarymanagment.DataModel;

import java.util.Date;

public class Visit {
    int id;
    int animalID;
    Date date;
    String obs;

    public Visit(int id, int animalID, Date date, String obs) {
        this.id = id;
        this.animalID = animalID;
        this.date = date;
        this.obs = obs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
