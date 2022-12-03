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

public class RegistrationController {

    @FXML
    public Text errorText;

    @FXML
    private TextField loginField;

    @FXML
    private Button authButton;

    @FXML
    private Button backButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registrationButton;

    private DatabaseManager databaseManager;

    private User user;

    public RegistrationController(){
        databaseManager = Main.getDatabaseManager();
        this.user = Main.getUser();
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
    void openAuthPage(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthController.class.getResource("../view/auth.fxml"));
        try {
            Stage stage = (Stage) authButton.getScene().getWindow();
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
    void register(ActionEvent event) {
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
            if (databaseManager.registerUser(login,hashed).equals("exist")){
                errorText.setText("Такой логин уже существует.\nПопробуйте другой");
                return;
            }
            User termUser = databaseManager.logIn(login,hashed);
            if (termUser == null){
                errorText.setText("произошла ошибка регистрации, попробуйте ещё раз");
                return;
            }
//            System.out.println(this.user.getName());
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
