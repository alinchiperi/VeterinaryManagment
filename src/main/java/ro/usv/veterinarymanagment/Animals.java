package ro.usv.veterinarymanagment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.usv.veterinarymanagment.DataModel.Animal;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class Animals implements Initializable {
    String jdbcURL = "jdbc:oracle:thin:@80.96.123.131:1521:ora09";

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String user = "hr";
    String passwd = "oracletest";
    @FXML
    TableView<Animal> tblAnimals;
    @FXML
    TextField txtOwner;
    @FXML
    TableColumn<Animal, Integer>colId;
    @FXML
    TableColumn<Animal, String>colName;
    @FXML
    TableColumn<Animal, Date>colBday;
    @FXML
    TableColumn<Animal, Date>colWeight;
    @FXML
    TableColumn<Animal, String>colSpecies;
    @FXML
    TableColumn<Animal, Integer>colOwnId;
    @FXML
    TextField txtId;
    @FXML
    TextField txtName;
    @FXML
    DatePicker datePick;
    @FXML
    TextField txtWeight;
    @FXML
    TextField txtSpecies;


    public void completeOwner(String nameOwner)
    {
        txtOwner.setText(nameOwner);
    }

    public void addAnimal(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();

            int id=0;
            String name = txtName.getText();
            LocalDate date = datePick.getValue();
            String weight = txtWeight.getText();
            String species = txtSpecies.getText();
            String owner=txtOwner.getText();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public ObservableList<Animal> getAnimals(){
        ObservableList<Animal> list = FXCollections.observableArrayList();
        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();
            String sqlCommand = "select * from animals_31a_ca";
            rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                list.add(new Animal(Integer.parseInt(rs.getString(1)), rs.getString(2),  formatter1.parse(rs.getString(3)), Integer.parseInt(rs.getString(4)), rs.getString(5), Integer.parseInt(rs.getString(6))));
            }
            stmt.close();
            rs.close();
            conn.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colBday.setCellValueFactory(new PropertyValueFactory<>("bday"));
            colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
            colSpecies.setCellValueFactory(new PropertyValueFactory<>("species"));
            colOwnId.setCellValueFactory(new PropertyValueFactory<>("owner"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
