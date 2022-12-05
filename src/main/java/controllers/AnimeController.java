package controllers;

import App.Main;
import data.Anime;
import data.Permissions;
import data.User;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AnimeController implements Initializable {

    @FXML
    private Button addToWatchedButton;

    @FXML
    private Button addToWatchingButton;

    @FXML
    private Button addToWillWatchButton;

    @FXML
    private Text descriptionText;

    @FXML
    private Text nameText;

    private Anime tempAnime;

    private DatabaseManager databaseManager;

    private User user;

    private int animeId;

    public AnimeController() {
        databaseManager = Main.getDatabaseManager();
        user = Main.getUser();
    }

    public void setAnimeId(int animeId) {
        this.animeId = animeId;
        try {
            tempAnime = databaseManager.getAnimeById(animeId);
            if ( tempAnime == null){
                nameText.setText("Нет данных");
                return;
            }
            insertValues(tempAnime.getName(),tempAnime.getDescription());
        } catch (SQLException e) {
            nameText.setText("Произошла ошибка");
        }
    }

    @FXML
    void addToWatched(ActionEvent event) {
        if (user.getPermission().equals(Permissions.GUEST)){
            openErrorPage();
            return;
        }
        if (tempAnime == null){
            return;
        }
        try {
            databaseManager.addToWatched(user,tempAnime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addToWatching(ActionEvent event) {
        if (user.getPermission().equals(Permissions.GUEST)){
            openErrorPage();
            return;
        }
        if (tempAnime == null){
            return;
        }
        try {
            databaseManager.addToWatching(user,tempAnime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addToWillWatch(ActionEvent event) {
        if (user.getPermission().equals(Permissions.GUEST)){
            openErrorPage();
            return;
        }
        if (tempAnime == null){
            return;
        }
        try {
            databaseManager.addToWillWatch(user,tempAnime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insertValues(String name, String description){
        nameText.setText(name);
        descriptionText.setText(description);
    }

    void openErrorPage(){
        Stage stage = new Stage();
        Scene scene = null;
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("../view/error.fxml"));
        try {
            scene = new Scene(fxmlLoader.load(), 600, 401);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
