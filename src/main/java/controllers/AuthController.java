package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthController {

    @FXML
    private TextField LoginField;

    @FXML
    private Button backButton;

    @FXML
    private javafx.scene.control.PasswordField PasswordField;

    @FXML
    private Button AuthButton;

    @FXML
    private Button RegistrationButton;

    @FXML
    void OpenRegistrationPage(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(RegistrationController.class.getResource("../view/registration.fxml"));
        try {
            Stage stage = (Stage) RegistrationButton.getScene().getWindow();
            stage.close();
            stage = new Stage();
            Scene scene = null;
            scene = new Scene(fxmlLoader.load(), 600, 401);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void back(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("../view/main.fxml"));
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            stage = new Stage();
            Scene scene = null;
            scene = new Scene(fxmlLoader.load(), 900, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Auth(ActionEvent event) {

    }
}
