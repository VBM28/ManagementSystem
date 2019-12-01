package adminPackage;


import db.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private TextField addIdTextField;
    @FXML
    private TextField addFirstNameTextField;
    @FXML
    private TextField addLastNameTextField;
    @FXML
    private TextField addEmailTextField;
    @FXML
    private DatePicker addDobDatePicker;
    @FXML
    private TableView<UserData> userTableView;
    @FXML
    private TableColumn<UserData, String> idColumn;
    @FXML
    private TableColumn<UserData, String> firstNameColumn;
    @FXML
    private TableColumn<UserData, String> lastNameColumn;
    @FXML
    private TableColumn<UserData, String> emailColumn;
    @FXML
    private TableColumn<UserData, String> dobColumn;
    @FXML
    private Label addEmployeeStatusLabel;

    private Datasource datasource;
    private Connection connection;
    private ResultSet result;
    private ObservableList<UserData> employeeData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initialize db
        this.datasource = new Datasource();
    }

    @FXML
    private void loadEmployeeData() {
        try {
            //loading db data
            connection = datasource.getConnectionString();
            this.employeeData = FXCollections.observableArrayList();

            //executing query
            result = connection.createStatement().executeQuery(Datasource.QUERY_MEMBERS);
            //retrieving each row
            while (result.next()) {
                //creating new user entry
                UserData employeeData = new UserData(result.getString(1), result.getString(2), result.getString(3),
                        result.getString(4), result.getString(5));
                //adding entry to the list
                this.employeeData.add(employeeData);
            }

        } catch (SQLException e) {
            System.out.println("SQLException in AdminController, loadEmployeeData : " + e.getMessage());

        } finally {
            //closing connections
            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException in AdminController, loadEmployeeData , couldn't close connection: " + e.getMessage());
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException in AdminController, loadEmployeeData , couldn't close connection: " + e.getMessage());
            }
        }

        //specifying how to populate the table view cells with UserData
        this.idColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("id"));
        this.firstNameColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("firstName"));
        this.lastNameColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("lastName"));
        this.emailColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("email"));
        this.dobColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("dob"));

        //setting the data in the table view
        this.userTableView.setItems(this.employeeData);
    }

    @FXML
    private void addEmployee() {
        PreparedStatement preparedStatement = null;
        try {
            //creating connection
            connection = datasource.getConnectionString();
            //preparing statement
            preparedStatement = connection.prepareStatement(Datasource.INSERT_MEMBER);
            preparedStatement.setString(1, this.addIdTextField.getText());
            preparedStatement.setString(2, this.addFirstNameTextField.getText());
            preparedStatement.setString(3, this.addLastNameTextField.getText());
            preparedStatement.setString(4, this.addEmailTextField.getText());
            preparedStatement.setString(5, this.addDobDatePicker.getEditor().getText());
            //executing query
            int update = preparedStatement.executeUpdate();
            //displaying that action was successful
            if (update != 0) {
                addEmployeeStatusLabel.setText(addFirstNameTextField.getText() + " " + addLastNameTextField.getText() + " was added .");
                //return true if succeeded

            }
        } catch (SQLException e) {
            System.out.println("SQLException/InterruptedException in AdminController: " + e.getMessage());

        } finally {
            //closing connections
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException in AdminController, addEmployee , couldn't close connection: " + e.getMessage());
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("SQLException in AdminController, addEmployee , couldn't close connection: " + e.getMessage());
            }
        }
    }


    @FXML
    private void clearTextFields() {
        this.addIdTextField.setText("");
        this.addFirstNameTextField.setText("");
        this.addLastNameTextField.setText("");
        this.addEmailTextField.setText("");
        this.addDobDatePicker.setValue(null);

    }

    @FXML
    private void deleteEmployee() {
        //storing the row number and retrieving the selected element from the table
        int row = userTableView.getSelectionModel().getSelectedIndex();
        UserData user = userTableView.getItems().get(row);
        //if no element selected, display information message
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No entry selected");
            alert.setContentText("Select an entry to delete");
            alert.showAndWait();
            return;
        }
        //if element selected
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting entry");
        alert.setContentText("Are you certain you want to delete " + user.getFirstName() + " " + user.getLastName() + " from the database?");

        Optional<ButtonType> choice = alert.showAndWait();

        //if deciding to delete contact, delete record from DB and tableview
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            //creating the task for the delete process
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    //initializing statement
                    PreparedStatement statement = null;
                    try {
                        //creating connection and preparing statement
                        connection = datasource.getConnectionString();
                        statement = connection.prepareStatement(Datasource.DELETE_MEMBER);
                        //setting parameters
                        statement.setString(1, user.getId());
                        statement.setString(2, user.getFirstName());
                        statement.setString(3, user.getLastName());
                        statement.setString(4, user.getEmail());
                        statement.setString(5, user.getDob());

                        //saving the result of the statement execution
                        int update = statement.executeUpdate();

                        //if statement successful, delete table view entry and refresh table
                        if (update != 0) {
                            userTableView.getItems().remove(row);
                            userTableView.refresh();
                            //if succeeded, return true, else false
                            return true;
                        }
                    } catch (SQLException e) {
                        System.out.println("SQLException in AdminController, deleteEmployee: " + e.getMessage());
                        return false;
                    } finally {
                        //closing connections
                        try {
                            if (statement != null) {
                                statement.close();
                            }
                        } catch (SQLException e) {
                            System.out.println("SQLException in AdminController, deleteEmployee; can't close connection: " + e.getMessage());
                        }
                        try {
                            if (connection != null) {
                                connection.close();
                            }
                        } catch (SQLException e) {
                            System.out.println("SQLException in AdminController, deleteEmployee; can't close connection: " + e.getMessage());
                        }
                    }
                    return false;
                }

            };
            //starting task
            new Thread(task).start();
        }
    }
}
