package controllers;


import App.Main;
import data.Anime;
import database.DatabaseManager;
import database.MySqlManager;
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

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private DatabaseManager databaseManager;
    @FXML
    private Button aboutButton;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

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
    private TableColumn<?, ?> watchedAnimeColumn;

    @FXML
    private TableView<?> watchedAnimeTable;

    @FXML
    private Button watchedButton;

    @FXML
    private Pane watchedPane;

    @FXML
    private TableColumn<?, ?> watchingAnimeColumn;

    @FXML
    private TableView<?> watchingAnimeTable;

    @FXML
    private Button watchingButton;

    @FXML
    private Pane watchingPane;

    @FXML
    private TableColumn<?, ?> willWatchAnimeColumn;

    @FXML
    private TableView<?> willWatchAnimeTable;

    @FXML
    private Button willWatchButton;

    @FXML
    private Pane willWatchPane;

    @FXML
    private ObservableList<Anime> animeList;

    public MainController(){
        databaseManager = Main.getDatabaseManager();
        animeList = FXCollections.observableArrayList();
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
    void add(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(AddController.class.getResource("../view/add.fxml"));
        try {
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
            stage = new Stage();
            Scene scene = null;
            scene = new Scene(fxmlLoader.load(), 350, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void OpenListAnimePane(ActionEvent event) {
        listAnimePane.setVisible(true);
        watchingPane.setVisible(false);
        willWatchPane.setVisible(false);
        watchedPane.setVisible(false);

        listAnimeButton.setStyle("-fx-background-color: #cd5700;");
        watchingButton.setStyle("-fx-background-color: #ec7c26;");
        willWatchButton.setStyle("-fx-background-color: #ec7c26;");
        watchedButton.setStyle("-fx-background-color: #ec7c26;");
    }

    @FXML
    void OpenWatchedPane(ActionEvent event) {
        listAnimePane.setVisible(false);
        watchingPane.setVisible(false);
        willWatchPane.setVisible(false);
        watchedPane.setVisible(true);

        listAnimeButton.setStyle("-fx-background-color: #ec7c26;");
        watchingButton.setStyle("-fx-background-color: #ec7c26;");
        willWatchButton.setStyle("-fx-background-color: #ec7c26;");
        watchedButton.setStyle("-fx-background-color: #cd5700;");
    }

    @FXML
    void OpenWatchingPane(ActionEvent event) {
        listAnimePane.setVisible(false);
        watchingPane.setVisible(true);
        willWatchPane.setVisible(false);
        watchedPane.setVisible(false);

        listAnimeButton.setStyle("-fx-background-color: #ec7c26;");
        watchingButton.setStyle("-fx-background-color: #cd5700;");
        willWatchButton.setStyle("-fx-background-color: #ec7c26;");
        watchedButton.setStyle("-fx-background-color: #ec7c26;");
    }

    @FXML
    void OpenWillWatchPane(ActionEvent event) {
        listAnimePane.setVisible(false);
        watchingPane.setVisible(false);
        willWatchPane.setVisible(true);
        watchedPane.setVisible(false);

        listAnimeButton.setStyle("-fx-background-color: #ec7c26;");
        watchingButton.setStyle("-fx-background-color: #ec7c26;");
        willWatchButton.setStyle("-fx-background-color: #cd5700;");
        watchedButton.setStyle("-fx-background-color: #ec7c26;");
    }

    @FXML
    void update(ActionEvent event) {

    }

    void recolor(Pane pane){  //todo разобраться
        listAnimePane.setVisible(false);
        watchingPane.setVisible(false);
        willWatchPane.setVisible(false);
        watchedPane.setVisible(false);

        listAnimeButton.setStyle("-fx-background-color: #ec7c26;");
        watchingButton.setStyle("-fx-background-color: #ec7c26;");
        willWatchButton.setStyle("-fx-background-color: #ec7c26;");
        watchedButton.setStyle("-fx-background-color: #ec7c26;");

        pane.setVisible(true);
        pane.setStyle("-fx-background-color: #cd5700;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animeColumn.setCellValueFactory(new PropertyValueFactory<Anime,String>("name"));
        try {
            animeList.addAll(databaseManager.getAnimeList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        animeList = FXCollections.observableArrayList(new Anime(1,"sao","cringe"));
//        animeList.add(new Anime(2,"sa2o","cri2nge"));
        animeTable.setItems(animeList);
//        System.out.println(animeList);
//        animeColumn = new TableColumn<>("name");
//        animeColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//        animeTable.getColumns().add(animeColumn);
//        animeTable.getItems().add(new Anime(1,"sao","cringe"));
//        animeTable.refresh();
    }
}
