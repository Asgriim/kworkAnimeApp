package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
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

    @FXML
    void addToWatched(ActionEvent event) {

    }

    @FXML
    void addToWatching(ActionEvent event) {

    }

    @FXML
    void addToWillWatch(ActionEvent event) {

    }

    void insertValues(String name, String description){
        nameText.setText(name);
        descriptionText.setText(description);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
