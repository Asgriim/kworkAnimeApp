package controllers;

import data.Anime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button aboutButton;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Anime,String> animeColumn;

    @FXML
    private TableView<Anime> animeTable;

    @FXML
    private Button authButton;

    @FXML
    private AnchorPane content;

    @FXML
    private Button listAnimeButton;

    @FXML
    private Pane listAnimePane;

    @FXML
    private Button removeButton;

    @FXML
    private Button watchedButton;

    @FXML
    private Pane watchedPane;

    @FXML
    private Button watchingButton;

    @FXML
    private Pane watchingPane;

    @FXML
    private Button willWatchButton;

    @FXML
    private Pane willWatchPane;

    @FXML
    private ObservableList<Anime> animeList;

    public MainController(){

    }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animeColumn.setCellValueFactory(new PropertyValueFactory<Anime,String>("name"));
        animeList = FXCollections.observableArrayList(new Anime(1,"sao","cringe"));
        animeList.add(new Anime(2,"sa2o","cri2nge"));
        animeTable.setItems(animeList);
//        System.out.println(animeList);
//        animeColumn = new TableColumn<>("name");
//        animeColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//        animeTable.getColumns().add(animeColumn);
//        animeTable.getItems().add(new Anime(1,"sao","cringe"));
//        animeTable.refresh();
    }
}
