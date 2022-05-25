package ro.usv.veterinarymanagment.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    @FXML
    Button btnTest;

    @FXML
    Label lblPets;

    @FXML
    Label lblOwners;

    @FXML
    private void testFunction(ActionEvent actionEvent){
        lblPets.setText("100");
        lblOwners.setText("4");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblOwners.setText("90");
    }
}
