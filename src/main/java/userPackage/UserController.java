package userPackage;

import db.Datasource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import login.LogInController;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    private Label idLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label dobLabel;

    private Datasource datasource;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet result;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            //initialize db
            datasource = new Datasource();
            //loading db data
            connection = datasource.getConnectionString();
            //executing query
            preparedStatement = connection.prepareStatement(Datasource.USER_MEMBERS_DETAILS);
            preparedStatement.setInt(1, LogInController.theId);
            result = preparedStatement.executeQuery();
            //setting the labels with the retrieved data
            while(result.next()){
                idLabel.setText(result.getString(1));
                firstNameLabel.setText(result.getString(2));
                lastNameLabel.setText(result.getString(3));
                emailLabel.setText(result.getString(4));
                dobLabel.setText(result.getString(5));
            }
        } catch (SQLException e) {
            System.out.println("SQLException in UserController, initialize: " + e.getMessage());
        }finally {
            //closing connection statements
            try{
                if(result!= null){
                    result.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException in UserController, initialize; couldn't close connection: " + e.getMessage());
            }
            try{
                if(preparedStatement!= null){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException in UserController, initialize; couldn't close connection: " + e.getMessage());
            }
            try{
                if(connection!= null){
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException in UserController, initialize; couldn't close connection: " + e.getMessage());
            }
        }
    }
}
