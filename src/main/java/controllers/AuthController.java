package controllers;

import App.Main;
import data.User;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AuthController {

    public Text errorText;
    @FXML
    private TextField loginField;

    @FXML
    private Button backButton;

    @FXML
    private javafx.scene.control.PasswordField passwordField;

    @FXML
    private Button AuthButton;

    @FXML
    private Button RegistrationButton;

    private DatabaseManager databaseManager;

    private User user;

    public AuthController(){
        databaseManager = Main.getDatabaseManager();
        this.user = Main.getUser();
    }

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
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login == null || login.equals("")){
            errorText.setText("Логин не может быть пустым");
            return;
        }
        login = login.strip();
        if(login.contains(" ")){
            errorText.setText("Логин не может содержать пробелы");
            return;
        }
        if (password == null || password.equals("")){
            errorText.setText("Пароль не может быть пустым");
            return;
        }
        if (password.contains(" ")){
            errorText.setText("Пароль не может содержать пробелы");
            return;
        }

        try {
            String hashed = Main.getHash(password);
            User termUser = databaseManager.logIn(login,hashed);
            if (termUser == null){
                errorText.setText("Неправильный логин или пароль");
                return;
            }
            this.user.setId(termUser.getId());
            this.user.setName(termUser.getName());
            this.user.setPassword(termUser.getPassword());
            this.user.setPermission(termUser.getPermission());
            back(new ActionEvent());
        } catch (SQLException e) {
            errorText.setText("Произошла серверная ошибка");
            e.printStackTrace();
        }
    }
}
