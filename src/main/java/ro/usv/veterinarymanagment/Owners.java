package ro.usv.veterinarymanagment;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.usv.veterinarymanagment.DataModel.Owner;
import java.sql.Connection;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Objects;
import java.util.ResourceBundle;

public class Owners extends Application implements Initializable {

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

    public void addOwner(ActionEvent event) {
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

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Input fields empty");
                alert.setContentText("Please fill all input fields");
                alert.show();
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
        txtId.setText(String.valueOf(own.getId()));
        txtFirstName.setText(own.getFirstName());
        txtLastName.setText(own.getLastName());
        txtPhone.setText(own.getPhone());
        txtEmail.setText(own.getEmail());
    }

    public void update(ActionEvent event)
    {
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
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(jdbcURL, user, passwd);
                stmt = conn.createStatement();

                String sqlCommand = "UPDATE owners_31a_ca SET ";

                sqlCommand +="first_name='"+firstName+"',"+"last_name='"+lastName+"',"+"phone='"+phone+"',"+"email='"+email+"'";

                sqlCommand += " WHERE id_owner= "+id;

                System.out.println(sqlCommand);

                int rezult = stmt.executeUpdate(sqlCommand);

                System.out.println("rezult = " + rezult);
                if(rezult>0)
                {
                    System.out.println("Update cu succes");

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
        return list;
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

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Owners.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        btnUpdate.setOnAction(a-> System.out.println("Alin"));
    }
}