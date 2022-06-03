package ro.usv.veterinarymanagment;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    @FXML
    Label lblPets;

    @FXML
    Label lblOwners;
    @FXML
    Label lblVisits;
    Connection conn = null;
    String jdbcURL = "jdbc:oracle:thin:@80.96.123.131:1521:ora09";
    Statement stmt = null;
    ResultSet rs = null;
    String user = "hr";
    String passwd = "oracletest";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();

            String sqlCountAnimals="select count(*)as total from animals_31a_ca";
            rs=stmt.executeQuery(sqlCountAnimals);
            if(rs.next())
            {
                lblPets.setText(String.valueOf(rs.getInt(1)));
            }
            String sqlCountOwners="select count(*)as total from owners_31a_ca";

            rs=stmt.executeQuery(sqlCountOwners);
            if(rs.next())
            {
                lblOwners.setText(String.valueOf(rs.getInt(1)));
            }
            /*String sqlCountVisits="select count(*)as total from owners_31a_ca";

            rs=stmt.executeQuery(sqlCountOwners);
            if(rs.next())
            {
                lblOwners.setText(String.valueOf(rs.getInt(1)));
            }*/


        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
}
