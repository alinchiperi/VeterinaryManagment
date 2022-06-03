package ro.usv.veterinarymanagment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ro.usv.veterinarymanagment.DataModel.Animal;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Optional;
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
    TableColumn<Animal, String>colBday;
    @FXML
    TableColumn<Animal, Integer>colWeight;
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void completeOwner(String nameOwner)
    {

        txtOwner.setText(nameOwner );
    }

    public void addAnimal() {

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();

            int id=0;
            String name = txtName.getText();
            LocalDate date = datePick.getValue();
            String bDate=formatter.format(date);

            String weight = txtWeight.getText();
            String species = txtSpecies.getText();
            String owner=txtOwner.getText().split(" ")[0];

            if(owner.equals("")&& weight.equals("")&& species.equals("") && name.equals("") && !txtId.getText().equals(""))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Input fields empty");
                alert.setContentText("Please fill all input fields");
                alert.show();
            }
            else {
                String sqlIdSecventa = "select secventa_animal_31A_CA.NEXTVAL from dual";
                rs = stmt.executeQuery(sqlIdSecventa);

                if (rs.next()) {
                    id = Integer.parseInt(rs.getString(1));
                }
                    Animal animal = new Animal(id, name, bDate, Float.parseFloat(weight),species, Integer.parseInt(owner) );
                    String sqlCommand = "insert into animals_31a_ca  values( ";
                    sqlCommand += id + ",'" + name+ "',TO_DATE('" + bDate + "','dd-MM-yyyy')," + weight + ",'" + species + "', "+ owner+ ")";

                   int rezult = stmt.executeUpdate(sqlCommand);

                if(rezult>0)
                {
                    System.out.println("inserat cu succes");
                    tblAnimals.getItems().add(animal);
                    clearInput();
                }
                else {
                    System.out.println("eroare");
                }
                System.out.println(sqlCommand);

            }
            stmt.close();
            rs.close();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void clearInput() {
        txtId.setText("");
        txtName.setText("");
        txtSpecies.setText("");
        txtWeight.setText("");
        datePick.getEditor().clear();
    }
    public void completeInput(){
        Animal animalSelected = tblAnimals.getSelectionModel().getSelectedItem();
        if(animalSelected!=null)
        {
            txtId.setText(String.valueOf(animalSelected.getId()));
            txtName.setText(animalSelected.getName());
//            datePick.setValue(LocalDate.parse(animalSelected.getBirthDate()));
            datePick.getEditor().setText(animalSelected.getBirthDate().substring(0,10));
            txtSpecies.setText(animalSelected.getSpecies());
            txtWeight.setText(String.valueOf(animalSelected.getWeight()));
        }

    }

    public void update(){
        String id= txtId.getText();
        String name = txtName.getText();

        String weight = txtWeight.getText();
        String species = txtSpecies.getText();

        if(id.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Select a pet");
            alert.setContentText("Please select an animal and press COMPLETE");
            alert.show();
        }
        else {
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();
                String sqlCommand = "UPDATE animals_31a_ca SET ";

                sqlCommand+="name = '"+name+"', weight= "+weight+", species= '"+species +"'";

                sqlCommand+= " WHERE id_animal ="+id;

                System.out.println(sqlCommand);

                int rezult = stmt.executeUpdate(sqlCommand);

                if(rezult>0)
                {
                    System.out.println("Update cu succes");

                    tblAnimals.setItems(getAnimals());
                    clearInput();

                }
                else {
                    System.out.println("eroare");
                }
                stmt.close();
                rs.close();
                conn.close();

            }
            catch ( Exception e) {
//                JOptionPane.showMessageDialog(null, "Exception" + e.getMessage());
                e.printStackTrace();
            }

        }
    }
    public ObservableList<Animal> getAnimals()
    {
        ObservableList<Animal> list = FXCollections.observableArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();
            String sqlCommand = "select * from animals_31a_ca";
            rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                Animal animal =(new Animal(Integer.parseInt(rs.getString(1)), rs.getString(2),rs.getString(3),rs.getFloat(4),rs.getString(5),rs.getInt(6) ));
                list.add(animal);
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
    public void deleteAnimal()
    {
        Animal animalForDelete = tblAnimals.getSelectionModel().getSelectedItem();
        if(animalForDelete==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Select an animal");
            alert.setContentText("Please select an animal and press DELETE");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete owner");
            alert.setContentText("Do you want delete "+ animalForDelete.getName()+" "+" ?");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if(confirmation.get() == ButtonType.OK)
            {

                try{
                    Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                    conn = DriverManager.getConnection(jdbcURL, user, passwd);
                    stmt = conn.createStatement();

                    String sqlDelete = "DELETE from animals_31a_ca WHERE id_animal= "+animalForDelete.getId();
                    System.out.println(sqlDelete);

                    int rezult =stmt.executeUpdate(sqlDelete);
                    if(rezult>0)
                    {
                        System.out.println("delete successful");
                        clearInput();
                        tblAnimals.setItems(getAnimals());
                    }
                    else {
                        System.out.println("eroare");
                    }
                    stmt.close();
                    rs.close();
                    conn.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public void addVisit(ActionEvent event) throws IOException {
        Parent root ;
        Scene scene ;
        Stage stage;
        Animal animalForVisit = tblAnimals.getSelectionModel().getSelectedItem();
        if(animalForVisit!=null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Visits.fxml"));
            root = loader.load();
            Visits visits = loader.getController();
            visits.completeIdAnimal(animalForVisit.getId()+" "+animalForVisit.getName());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene= new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colBday.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
            colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
            colSpecies.setCellValueFactory(new PropertyValueFactory<>("species"));
            colOwnId.setCellValueFactory(new PropertyValueFactory<>("ownerId"));

            ObservableList<Animal> list= getAnimals();
            tblAnimals.setItems(list);

        }
        catch (Exception e)
        {
//            e.printStackTrace();
            System.err.println(e.getMessage());
        }

    }
}

