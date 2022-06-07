package ro.usv.veterinarymanagment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    @FXML
    Label lblPets;

    @FXML
    Label lblOwners;
    @FXML
    Label lblVisits;
    @FXML Label lblSpecies;
    @FXML
    PieChart pieChart;
    Connection conn = null;
    String jdbcURL = "jdbc:oracle:thin:@80.96.123.131:1521:ora09";
    Statement stmt = null;
    ResultSet rs = null;
    String user = "hr";
    String passwd = "oracletest";

    HashMap<String, Integer> mapSpecies = new HashMap<>();

    public HashMap<String, Integer> getMapSpecies(){
        HashMap<String, Integer> map = new HashMap<>();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            stmt = conn.createStatement();

            String sqlCommand="Select Upper(species), Count(Upper(species)) from animals_31a_ca group by upper(species)";
            rs=stmt.executeQuery(sqlCommand);
            while (rs.next())
            {
                map.put(rs.getString(1),rs.getInt(2));
            }

            stmt.close();
            rs.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
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
            String sqlCountVisits="select count(*)as total from visits_31a_ca";

            rs=stmt.executeQuery(sqlCountVisits);
            if(rs.next())
            {
                lblVisits.setText(String.valueOf(rs.getInt(1)));
            }
            String sqlCountSpecies ="select count(distinct UPPER(species)) from animals_31a_ca";
            rs=stmt.executeQuery(sqlCountSpecies);
            if(rs.next())
            {
                lblSpecies.setText(String.valueOf(rs.getInt(1)));
            }

            mapSpecies=getMapSpecies();

            ObservableList<PieChart.Data> listOfSpecies = FXCollections.observableArrayList();
            for (String i :
                    mapSpecies.keySet()) {
                listOfSpecies.add(new PieChart.Data(i+" ",mapSpecies.get(i)));
            }
            pieChart.setData(listOfSpecies);
            int sum = mapSpecies.values().stream().reduce(0, Integer::sum);

            pieChart.getData().forEach(data -> {
                        String percentage = String.format("%.2f%%", ((data.getPieValue()*100) / sum));
                        Tooltip toolTip = new Tooltip(percentage);
                        Tooltip.install(data.getNode(), toolTip);
                    });
            stmt.close();
            rs.close();
            conn.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
