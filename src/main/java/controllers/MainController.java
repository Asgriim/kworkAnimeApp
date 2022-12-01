package controllers;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class MainController {

    @FXML
    private Button AboutButton;

    @FXML
    private Button AddButton;

    @FXML
    private TableColumn<?,?> AnimeColumn;

    @FXML
    private TableView<?> AnimeTable;

    @FXML
    private Button AuthButton;

    @FXML
    private AnchorPane Content;

    @FXML
    private Button ListAnimeButton;

    @FXML
    private Pane ListAnimePane;

    @FXML
    private Button RemoveButton;

    @FXML
    private Button WatchedButton;

    @FXML
    private Pane WatchedPane;

    @FXML
    private Button WatchingButton;

    @FXML
    private Pane WatchingPane;

    @FXML
    private Button WillWatchButton;

    @FXML
    private Pane WillWatchPane;

    @FXML
    void OpenAboutPane(ActionEvent event) {
        Stage stage = new Stage();
        Scene scene = null;
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("../view/about.fxml"));
        try {
            scene = new Scene(fxmlLoader.load(), 600, 401);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void OpenAuthPane(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("../view/auth.fxml"));
        try {
            Stage stage = (Stage) AuthButton.getScene().getWindow();
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
    void OpenListAnimePane(ActionEvent event) {

    }

    @FXML
    void OpenWatchedPane(ActionEvent event) {

    }

    @FXML
    void OpenWatchingPane(ActionEvent event) {

    }

    @FXML
    void OpenWillWatchPane(ActionEvent event) {

    }

}
