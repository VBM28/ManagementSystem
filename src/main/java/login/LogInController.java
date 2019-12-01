package login;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private Label dbStatusLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<Option> selectionComboBox;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginStatusLabel;

    public static int theId = -2;

    LogInModel logInModel = new LogInModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //connecting to DB
        if (this.logInModel.isDbConnected()) {
            this.dbStatusLabel.setText("Connected");
        } else {
            this.dbStatusLabel.setText("Not connected");
        }
        //setting combo box with enum values
        this.selectionComboBox.setItems(FXCollections.observableArrayList(Option.values()));

    }
    @FXML
    public void logIn() {
        try {
            //saving the result of the query
            int entry =this.logInModel.isLogin(this.usernameTextField.getText(),
                    this.passwordField.getText(), this.selectionComboBox.getValue().toString());
            //checking if login was successful
            if (entry != -1) {
                //passing the value to theId for future queries
                theId = entry;
                //open new stage and close login one
                Stage stage = (Stage) this.loginButton.getScene().getWindow();
                stage.close();

                switch (this.selectionComboBox.getValue().toString()) {
                    case "Admin":
                        adminLogin();
                        break;
                    case "User":
                        userLogin();
                        break;
                }
            } else {
                loginStatusLabel.setText("Incorrect account details/ No account");
            }

        } catch (Exception e) {
            System.out.println("Exception in LoginController: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    public void userLogin() {
        try {
            //opening window for user
            Stage userStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/userPackage/userfxml.fxml").openStream());

            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle("User Panel");
            userStage.show();

        } catch (IOException e) {
            System.out.println("IOException in LoginController: " + e.getMessage());
        }
    }


    public void adminLogin() {
        try {
            //opening window for admin
            Stage adminStage = new Stage();
            Pane root = FXMLLoader.load(getClass().getResource("/adminPackage/adminfxml.fxml"));

            Scene scene = new Scene(root);
            adminStage.setScene(scene);
            adminStage.setTitle("Admin Panel");
            adminStage.show();
        } catch (IOException e) {
            System.out.println("IOException in LoginController: " + e.getMessage());

        }
    }
}
