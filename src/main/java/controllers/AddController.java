package controllers;

import App.Main;
import data.Anime;
import data.User;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AddController {

    public Text errorText;
    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField nameField;

    private DatabaseManager databaseManager;

    private User user;

    public AddController(){
        this.databaseManager = Main.getDatabaseManager();
        this.user = Main.getUser();
    }

    @FXML
    void add(ActionEvent event) {
        String name = nameField.getText();
        String desc = descriptionField.getText();
        if (name == null || name.equals("")){
            errorText.setText("Добавьте название");
            return;
        }
        if (desc == null || desc.equals("")){
            errorText.setText("Добавьте описание");
            return;
        }
        Anime anime = new Anime(0,name,desc);

        try {
            if(databaseManager.addAnime(user,anime).equals("ok")){
                errorText.setText("Добавлено успешно");
            };
        } catch (SQLException e) {
            e.printStackTrace();
            errorText.setText("Серверная ошибка.\nПопробуйте позже.");
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

}
