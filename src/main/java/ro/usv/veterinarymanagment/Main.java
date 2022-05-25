package ro.usv.veterinarymanagment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Veterinary Managment !");
        stage.setScene(scene);
        stage.show();
    }

}
