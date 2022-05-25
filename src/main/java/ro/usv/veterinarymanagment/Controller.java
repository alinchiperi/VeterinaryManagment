package ro.usv.veterinarymanagment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button btnAnimals;

    @FXML
    private Button btnDashBoard;

    @FXML
    private Button btnOwners;

    @FXML
    private Button btnVisits;

    @FXML
    private void handleButtonClicks(ActionEvent actionEvent){
        if(actionEvent.getSource()==btnDashBoard){
            loadStage("Dashboard.fxml");
        }
        else if (actionEvent.getSource()==btnOwners){
            loadStage("Owners.fxml");
        }
        else if (actionEvent.getSource()==btnAnimals){
            loadStage("Animals.fxml");
        }
        else if(actionEvent.getSource()==btnVisits){
            loadStage("Visits.fxml");
        }
    }
    private void loadStage(String fxml)
    {
        try{
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("eroare "+e.getMessage()+ e.getCause());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public static class Animals {

        @FXML
        TextField txtId;

    }
}
