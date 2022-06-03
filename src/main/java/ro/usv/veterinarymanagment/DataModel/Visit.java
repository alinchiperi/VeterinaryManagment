package ro.usv.veterinarymanagment.DataModel;



public class Visit {
    int id;
    int animalID;
    String date;
    String obs;
    String name;

    public Visit(int id, int animalID,  String obs, String date) {
        this.id = id;
        this.animalID = animalID;
        this.date = date;
        this.obs = obs;
    }

    public Visit(int id, int animalID, String date, String obs, String name) {
        this.id = id;
        this.animalID = animalID;
        this.date = date;
        this.obs = obs;
        this.name = name;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
