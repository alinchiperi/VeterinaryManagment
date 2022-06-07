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
import ro.usv.veterinarymanagment.DataModel.Visit;

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

public class Visits implements Initializable {
    String jdbcURL = "jdbc:oracle:thin:@80.96.123.131:1521:ora09";

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String user = "hr";
    String passwd = "oracletest";
    @FXML
    TableView<Visit> tblVisits;

    @FXML
    ComboBox<String> cbObs;
    @FXML
    TextField txtId;
    @FXML TextField txtAnimalId;

    @FXML
    DatePicker datePicker;
    @FXML Label lblTotal;
    @FXML TableColumn<Visit, Integer> colId;
    @FXML TableColumn<Visit, String> colAnimalId;
    @FXML TableColumn<Visit, String> colObs;
    @FXML TableColumn<Visit, String> colDate;
    @FXML CheckBox ckbToday;
    @FXML TableColumn<Visit, String>colName;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    ObservableList<String> visitsList = FXCollections.observableArrayList("Control","Vaccin","Ingrijire","Castrare","Operatie","Recuperare");

    public void completeIdAnimal(String name)
    {
        txtAnimalId.setText(name);
    }
    public void addVisit(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();

            int id=0;
            LocalDate date;
            String visitDate;

            String idAnimal = txtAnimalId.getText().split(" ")[0];
            if(ckbToday.isSelected()){
                 date= LocalDate.now();
                 visitDate=formatter.format(date);
                 datePicker.getEditor().setText(visitDate);
            }
            else {
                date = datePicker.getValue();
                visitDate = formatter.format(date);
            }

            String obs = cbObs.getValue();

            if (!visitDate.equals("") && !obs.equals("") && idAnimal.equals("")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Input fields empty");
                alert.setContentText("Please fill all input fields");
                alert.show();
            }
            else {
                String sqlIdSecventa = "select secventa_visit_31A_CA.NEXTVAL from dual";
                rs = stmt.executeQuery(sqlIdSecventa);

                if (rs.next()) {
                    id = Integer.parseInt(rs.getString(1));
                }
                if(id!=0){
                    Visit visit = new Visit(id,Integer.parseInt(idAnimal),obs,visitDate);
                    String sqlCommand="insert into visits_31a_ca  values( ";
                    sqlCommand+= id+","+idAnimal+",'"+obs+"',TO_DATE('"+visitDate+"','dd-MM-yyyy'))";

                    int rezult = stmt.executeUpdate(sqlCommand);
                    if(rezult>0)
                    {
                        System.out.println("inserat cu succes");
                        tblVisits.setItems(getVisits());
                        clearInput();
                    }
                    else {
                        System.out.println("eroare");
                    }
                }
            }
            stmt.close();
            rs.close();
            conn.close();
        }
        catch (Exception e){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
    public void completeInput(){
        Visit visit= tblVisits.getSelectionModel().getSelectedItem();
        System.out.println(visit);
        if(visit!=null){
            txtId.setText(String.valueOf(visit.getId()));
            txtAnimalId.setText(String.valueOf(visit.getAnimalID()));
            cbObs.getSelectionModel().select(visit.getObs());
            datePicker.getEditor().setText(visit.getDate());
        }
        else {
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Select a visit");
            alert.setContentText("Please select a visit and pres COMPLETE");
            alert.show();
        }
    }

    private void clearInput() {
        txtId.setText("");
        txtAnimalId.setText("");
        datePicker.getEditor().clear();
        cbObs.getSelectionModel().clearSelection();
    }

    public void deleteVisit(){
        Visit visitForDelete=tblVisits.getSelectionModel().getSelectedItem();
        if(visitForDelete!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete visit");
            alert.setContentText("Do you want to delete this visit?");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if(confirmation.get() == ButtonType.OK)
            {
                try {

                    Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                    conn = DriverManager.getConnection(jdbcURL, user, passwd);
                    stmt = conn.createStatement();

                    String sqlDelete = "DELETE from visits_31a_ca WHERE id_visit= " + visitForDelete.getId();
                    int rezult =stmt.executeUpdate(sqlDelete);
                    if(rezult>0)
                    {
                        System.out.println("delete successful");
                        clearInput();
                        tblVisits.setItems(getVisits());
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
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Select a visit");
            alert.setContentText("And then press delete");
            alert.show();
        }

    }
    public void updateVisit(){
        String id= txtId.getText();
        if(id.equals("")){
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Select a visit");
            alert.setContentText("Please select a visit and pres COMPLETE");
            alert.show();
        }
        else {
            String animalId=txtAnimalId.getId();
            String obs = cbObs.getValue();
            String date= datePicker.getEditor().getText();
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();
                String sqlCommand = "UPDATE visits_31a_ca SET ";
                sqlCommand+= "obs= '"+obs+"'"+ "Where id_visit="+id;
                int rezult = stmt.executeUpdate(sqlCommand);

                if(rezult>0) {
                    System.out.println("Update cu succes");
                    tblVisits.getItems();
                    clearInput();
                }
                else {
                    System.out.println("eroare");
                }
                stmt.close();
                rs.close();
                conn.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public ObservableList<Visit>getVisits(){
        ObservableList<Visit> list = FXCollections.observableArrayList();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();

            String sqlCommand = "select v.*, a.name from visits_31a_ca v, animals_31a_ca a Where v.id_animal=a.id_animal";
            rs = stmt.executeQuery(sqlCommand);

            while (rs.next()){
                list.add(new Visit(rs.getInt(1), rs.getInt(2),rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            stmt.close();
            rs.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        lblTotal.setText("Total: "+list.size());
        return list;
    }
    public void viewPet(ActionEvent event) throws IOException {
        /*Parent root ;
        Scene scene ;
        Stage stage;
        Visit visitSelected = tblVisits.getSelectionModel().getSelectedItem();
        if(visitSelected!=null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Animals.fxml"));
            root = loader.load();
            Animals animals = loader.getController();
            animals.showAnimal(visitSelected.getAnimalID());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene= new Scene(root);
            stage.setScene(scene);
            stage.show();
        }*/
        ObservableList<Visit> list = FXCollections.observableArrayList();
        Visit visit = tblVisits.getSelectionModel().getSelectedItem();
        if(visit==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Select a item from table");
            alert.setContentText("Select a item and press view pet");
            alert.show();
        }
        else {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();


                String sqlCommand = "select v.*, a.name from visits_31a_ca v, animals_31a_ca a Where v.id_animal=a.id_animal and v.id_animal =" + visit.getAnimalID();
                rs = stmt.executeQuery(sqlCommand);

                while (rs.next()) {
                    list.add(new Visit(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                }
                tblVisits.setItems(list);
                lblTotal.setText("Total: " + list.size());
                stmt.close();
                rs.close();
                conn.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbObs.setItems(visitsList);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAnimalId.setCellValueFactory(new PropertyValueFactory<>("animalID"));
        colObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colName.setCellValueFactory(new PropertyValueFactory<>("animalName"));
        tblVisits.setItems(getVisits());
    }
}
