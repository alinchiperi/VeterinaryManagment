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
import ro.usv.veterinarymanagment.DataModel.Owner;

import java.io.IOException;
import java.sql.Connection;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Owners implements Initializable {

    String jdbcURL = "jdbc:oracle:thin:@80.96.123.131:1521:ora09";

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String user = "hr";
    String passwd = "oracletest";
    @FXML
    TextField txtId;
    @FXML
    TextField txtFirstName;
    @FXML
    TextField txtLastName;
    @FXML
    TextField txtPhone;
    @FXML
    TextField txtEmail;
    @FXML
    TableView<Owner> tblOwners;
    @FXML
    TableColumn<Owner, Integer> colId;
    @FXML
    TableColumn<Owner, String> colFirstName;
    @FXML
    TableColumn<Owner, String> colLastName;
    @FXML
    TableColumn<Owner, String> colPhone;
    @FXML
    TableColumn<Owner, String> colEmail;
    @FXML
    Button btnUpdate;
    @FXML
    Label lblTotal;

    String regexEmail = "^(.+)@(.+)$";
    String regexPhone ="^(\\+4|)?(07[0-9]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}$";
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public void addOwner() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();

            int id = 0;
            String firstName =txtFirstName.getText();
            String lastName = txtLastName.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();

            if((firstName.equals("") || lastName.equals("") || phone.equals("") || email.equals(""))) {
                errorInput();
            } else if (!patternMatches(phone, regexPhone)) {
                errorPhone();

                
            } else if(!patternMatches(email, regexEmail)){
                errorEmail();

            }
            else
            {
                String sqlIdSecventa = "select secventa_owner_31A_CA.NEXTVAL from dual";
                rs = stmt.executeQuery(sqlIdSecventa);

                if (rs.next()) {
                    id = Integer.parseInt(rs.getString(1));
                }

                Owner owner = new Owner(id, firstName, lastName, phone, email);
                String sqlCommand = "insert into owners_31a_Ca (id_owner, first_name, last_name, phone, email) values( ";
                sqlCommand += id + ",'" + firstName + "','" + lastName + "','" + phone + "','" + email + "')";

                System.out.println(sqlCommand);

                int rezult = stmt.executeUpdate(sqlCommand);
                if(rezult>0)
                {
                    System.out.println("inserat cu succes");

                }
                else {
                    System.out.println("eroare");
                }
                tblOwners.getItems().add(owner);

                clearInput();
            }

                stmt.close();
                rs.close();
                conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void completeInput() {
        Owner own = tblOwners.getSelectionModel().getSelectedItem();
        if(own!=null) {
            txtId.setText(String.valueOf(own.getId()));
            txtFirstName.setText(own.getFirstName());
            txtLastName.setText(own.getLastName());
            txtPhone.setText(own.getPhone());
            txtEmail.setText(own.getEmail());
        }
    }
    public void update() {
        String id = txtId.getText();

        String firstName =txtFirstName.getText();
        String lastName = txtLastName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        if(id.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Select an owner");
            alert.setContentText("Please select an owner and press COMPLETE");
            alert.show();
        }
        else {
            if((firstName.equals("") || lastName.equals("") || phone.equals("") || email.equals(""))) {
                errorInput();
            } else if (!patternMatches(phone, regexPhone)) {
                errorPhone();


            } else if(!patternMatches(email, regexEmail)){
                errorEmail();
            }
            else
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();

                String sqlCommand = "UPDATE owners_31a_ca SET ";

                sqlCommand +="first_name='"+firstName+"',"+"last_name='"+lastName+"',"+"phone='"+phone+"',"+"email='"+email+"'";

                sqlCommand += " WHERE id_owner= "+id;

                int rezult = stmt.executeUpdate(sqlCommand);

                System.out.println("rezult = " + rezult);
                if(rezult>0)
                {
                    System.out.println("Update cu succes");

                    tblOwners.setItems(getOwners());
                    clearInput();

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
    public void delete(){
        Owner ownerForDelete = tblOwners.getSelectionModel().getSelectedItem();
            if (ownerForDelete == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Select an owner");
                alert.setContentText("Please select an owner and press DELETE");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete owner");
                alert.setContentText("Do you want delete "+ ownerForDelete.getFirstName()+" "+ ownerForDelete.getLastName()+" ?");
                Optional <ButtonType> confirmation = alert.showAndWait();
                if(confirmation.get() == ButtonType.OK)
                {

                    try{
                        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                        conn = DriverManager.getConnection(jdbcURL, user, passwd);
                        stmt = conn.createStatement();

                        String sqlDelete = "DELETE from owners_31a_ca WHERE id_owner= "+ownerForDelete.getId();
                        System.out.println(sqlDelete);

                        int rezult =stmt.executeUpdate(sqlDelete);
                        if(rezult>0)
                        {
                            System.out.println("delete successful");
                            clearInput();
                            tblOwners.setItems(getOwners());
                        }
                        else {
                            System.out.println("eroare");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
    }
    public void clearInput() {
        txtId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }
    public ObservableList<Owner> getOwners(){
        ObservableList<Owner> list = FXCollections.observableArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();
            String sqlCommand = "select * from owners_31a_ca";
            rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                list.add(new Owner(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            stmt.close();
            rs.close();
            conn.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setLabelTotal(String.valueOf(list.size()));

        return list;
    }

    public void viewAllOwners(){
        tblOwners.setItems(getOwners());
    }

    public void findOwner(){
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        ObservableList<Owner> list = FXCollections.observableArrayList();

        if(!firstName.equals("") && !lastName.equals(""))
        {
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();
                String sqlCommand = "Select * from owners_31a_ca where ";
                sqlCommand += "UPPER('"+ firstName+"')= UPPER(first_name) AND  UPPER(last_name) = UPPER('"+lastName+"')";
                rs=stmt.executeQuery(sqlCommand);
                while (rs.next())
                {
                    list.add(new Owner(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                }
                tblOwners.setItems(list);
                clearInput();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Find Owner");
            alert.setContentText("Please complete first name and last name ");
            alert.show();
        }
    }
    public void addPet(ActionEvent event) throws IOException {

        Parent root ;
        Scene scene ;
        Stage stage;
        Owner owner = tblOwners.getSelectionModel().getSelectedItem();
        if(owner!=null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Animals.fxml"));
            root = loader.load();
            Animals animals = loader.getController();
            animals.completeOwner(owner.getId()+" "+ owner.getLastName()+" "+owner.getFirstName());
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene= new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Select an owner");
            alert.setContentText("Please select an owner");
            alert.show();
        }
    }
    public void showOwner(int id) {
        ObservableList <Owner> list= FXCollections.observableArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();
            String sqlCommand= "Select * from owners_31a_ca WHERE id_owner= "+id;
            rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                list.add(new Owner(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            tblOwners.setItems(list);
            stmt.close();
            rs.close();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void findPets(ActionEvent event) throws IOException {
        Parent root;
        Scene scene;
        Stage stage;
        Owner ownerPets = tblOwners.getSelectionModel().getSelectedItem();
        if (ownerPets != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Animals.fxml"));
            root = loader.load();
            Animals animals = loader.getController();
            animals.findOwnerPets(ownerPets.getId());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
    public void setLabelTotal(String total){
        lblTotal.setText("Total: "+total);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            ObservableList<Owner> list = getOwners();
            tblOwners.setItems(list);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void errorEmail(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid email");
        alert.setContentText("Please type a valid email");
        alert.show();
    }
    void errorInput(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input fields empty");
        alert.setContentText("Please fill all input fields");
        alert.show();
    }
    void errorPhone(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid phone number");
        alert.setContentText("Please type a valid phone number");
        alert.show();
    }
}
