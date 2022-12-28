package controllers;

import App.Main;
import data.Anime;
import data.AnimeReview;
import data.User;
import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddReviewController {

    @FXML
    private Button addButton;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Text errorText;

    private DatabaseManager databaseManager;

    private User user;

    private Anime currAnime;

    public void setCurrAnime(Anime currAnime) {
        this.currAnime = currAnime;
    }

    @FXML
    void add(ActionEvent event) {
        String comment = descriptionField.getText();
        comment = comment.strip();
        if (comment == null || comment.equals("")){
            errorText.setText("Отзыв не может быть пустым");
            return;
        }
        if (comment.length() > 500){
            errorText.setText("Отзыв должен быть до 500 символов");
            return;
        }
        try {
            databaseManager.addAnimeReview(user,currAnime,comment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }

    public AddReviewController() {
        this.databaseManager = Main.getDatabaseManager();
        this.user = Main.getUser();
    }
}
