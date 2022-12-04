package controllers;


import App.Main;
import data.Anime;
import data.AnimeStatus;
import data.Permissions;
import data.User;
import database.DatabaseManager;
import database.MySqlManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
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
    private TableColumn<Anime,String> watchedAnimeColumn;

    @FXML
    private TableView<Anime> watchedAnimeTable;

    @FXML
    private Button watchedButton;

    @FXML
    private Pane watchedPane;

    @FXML
    private TableColumn<Anime,String> watchingAnimeColumn;

    @FXML
    private TableView<Anime> watchingAnimeTable;

    @FXML
    private Button watchingButton;

    @FXML
    private Pane watchingPane;

    @FXML
    private TableColumn<Anime,String> willWatchAnimeColumn;

    @FXML
    private TableView<Anime> willWatchAnimeTable;

    @FXML
    private Button willWatchButton;

    @FXML
    private Pane willWatchPane;

    @FXML
    private ObservableList<Anime> animeList;
    @FXML
    private ObservableList<Anime> willWatchAnimeList;
    @FXML
    private ObservableList<Anime> watchingAnimeList;
    @FXML
    private ObservableList<Anime> watchedAnimeList;
    
    private User user;
    
    public MainController(){
        this.databaseManager = Main.getDatabaseManager();
        this.animeList = FXCollections.observableArrayList();
        this.watchingAnimeList = FXCollections.observableArrayList();
        this.willWatchAnimeList = FXCollections.observableArrayList();
        this.watchedAnimeList = FXCollections.observableArrayList();
        this.user = Main.getUser();
        System.out.println(user);
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
        // TODO: 03.12.2022 добавить предложение зарегаться при попытке нажатия гостя
        recolor(listAnimePane,listAnimeButton);
    }

    @FXML
    void OpenWatchedPane(ActionEvent event) {
        recolor(watchedPane,watchedButton);
    }

    @FXML
    void OpenWatchingPane(ActionEvent event) {
        recolor(watchingPane,watchingButton);
    }

    @FXML
    void OpenWillWatchPane(ActionEvent event) {
        recolor(willWatchPane,willWatchButton);
    }

    @FXML
    void update(ActionEvent event) {
        if(user.getPermission().equals(Permissions.GUEST)){
            animeList.clear();
            try {
                animeList.addAll(databaseManager.getAnimeList());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }
        animeList.clear();
        watchedAnimeList.clear();
        willWatchAnimeList.clear();
        watchingAnimeList.clear();
        try {
            animeList.addAll(databaseManager.getAnimeList());
            watchedAnimeList.addAll(databaseManager.getUserAnimes(user, AnimeStatus.WATCHED));
            watchingAnimeList.addAll(databaseManager.getUserAnimes(user, AnimeStatus.WATCHING));
            willWatchAnimeList.addAll(databaseManager.getUserAnimes(user, AnimeStatus.WILL_WATCH));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    void recolor(Pane pane,Button button){
        listAnimePane.setVisible(false);
        watchingPane.setVisible(false);
        willWatchPane.setVisible(false);
        watchedPane.setVisible(false);

        listAnimeButton.setStyle("-fx-background-color: #ec7c26;");
        watchingButton.setStyle("-fx-background-color: #ec7c26;");
        willWatchButton.setStyle("-fx-background-color: #ec7c26;");
        watchedButton.setStyle("-fx-background-color: #ec7c26;");

        pane.setVisible(true);
        button.setStyle("-fx-background-color: #cd5700;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: 03.12.2022 добавить сюда обновления интерфейса для юзера или админа
        animeColumn.setCellValueFactory(new PropertyValueFactory<Anime,String>("name"));
        watchedAnimeColumn.setCellValueFactory(new PropertyValueFactory<Anime,String>("name"));
        watchingAnimeColumn.setCellValueFactory(new PropertyValueFactory<Anime,String>("name"));
        willWatchAnimeColumn.setCellValueFactory(new PropertyValueFactory<Anime,String>("name"));
        try {
            animeList.addAll(databaseManager.getAnimeList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        animeTable.setItems(animeList);
        if (user.getPermission().equals(Permissions.GUEST)){
            hideAdminButtons();
            return;
        }
        try {
            watchedAnimeList.addAll(databaseManager.getUserAnimes(user, AnimeStatus.WATCHED));
            watchingAnimeList.addAll(databaseManager.getUserAnimes(user, AnimeStatus.WATCHING));
            willWatchAnimeList.addAll(databaseManager.getUserAnimes(user, AnimeStatus.WILL_WATCH));
            watchedAnimeTable.setItems(watchedAnimeList);
            watchingAnimeTable.setItems(watchingAnimeList);
            willWatchAnimeTable.setItems(willWatchAnimeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user.getPermission().equals(Permissions.ADMIN)){
            showAdminButtons();
        }
        else {
            hideAdminButtons();
        }
    }

    private void hideAdminButtons(){
        removeButton.setVisible(false);
        addButton.setVisible(false);
    }

    private void showAdminButtons(){
        removeButton.setVisible(true);
        addButton.setVisible(true);
    }

    Anime temp;
    Date lastClickTime;
    @FXML
    void handleRowSelect(MouseEvent event) {
        Anime row = animeTable.getSelectionModel().getSelectedItem();
        if (row == null) return;
        if(row != temp){
            temp = row;
            lastClickTime = new Date();
        } else {
            Date now = new Date();
            long diff = now.getTime() - lastClickTime.getTime();
            if (diff < 300){
                FXMLLoader loader1 = new FXMLLoader();
                loader1.setLocation(getClass().getResource("../view/anime.fxml"));
                try {
                    loader1.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader1.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                AnimeController controller = loader1.getController();
                controller.insertValues(row.getName(),row.getDescription());
                stage.showAndWait();

            } else {
                lastClickTime = new Date();
            }
        }
    }
}
