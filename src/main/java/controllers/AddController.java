package controllers;

import App.Main;
import data.Anime;
import data.AnimePicture;
import data.ImageType;
import data.User;
import database.DatabaseManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddController implements Initializable {

    public Text errorText;
    @FXML
    public Button addFrameButton;
    @FXML
    public Button addPosterButton;
    @FXML
    public TableView<AnimePicture> filesTable;
    @FXML
    public TableColumn<AnimePicture, String> fileNameColumn;
    @FXML
    public TableColumn<AnimePicture, Button> deleteColumn;
    @FXML
    public TableColumn<AnimePicture, String> typeColumn;
    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField nameField;

    private DatabaseManager databaseManager;

    ObservableList<AnimePicture> fileList;

    private User user;

    public AddController(){
        this.databaseManager = Main.getDatabaseManager();
        this.user = Main.getUser();
        this.fileList = FXCollections.observableArrayList();
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
        if (name.length() > 60){
            errorText.setText("Название не может быть больше 60 символов");
            return;
        }
        if (desc.length() > 800){
            errorText.setText("Описание не может быть больше 800 символов");
            return;
        }
        Anime anime = new Anime(0,name.strip(),desc.strip());

        try {
            AnimePicture poster = fileList.stream().filter(a -> a.getType().equals(ImageType.POSTER)).findAny().orElse(null);
            if (poster == null){
                errorText.setText("Не выбран постер.");
                return;
            }

            List<AnimePicture> frames = fileList.stream().filter(a -> a.getType().equals(ImageType.FRAME)).collect(Collectors.toList());

            if (frames.size() < 1) {
                errorText.setText("Нужно добавить хотя бы 1 кадр");
                return;
            }

            if(databaseManager.addAnime(user,anime,poster,frames).equals("ok")){
                errorText.setText("Добавлено успешно");
                databaseManager.adminLog(user,anime,"add anime");
            }
            else
                errorText.setText("Произошла ошибка");
        } catch (SQLException e) {
            e.printStackTrace();
            errorText.setText("Серверная ошибка.\nПопробуйте позже.");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("../view/main.fxml"));
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            stage = new Stage();
            Scene scene = null;
            scene = new Scene(fxmlLoader.load(), 900, 600);
            stage.setScene(scene);
            stage.setResizable(false);
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
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void addFrame(ActionEvent event) throws FileNotFoundException {
        if (fileList.stream().filter(a -> a.getType().equals(ImageType.FRAME)).count() >= 10){
            errorText.setText("Нельзя добавить больше 10 изображений");
            return;
        }
        errorText.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open frame image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null){
            errorText.setText("Неверный файл");
            return;
        }
        if (selectedFile.length() >= 16777216 - 100){
            errorText.setText("Файл не может быть больше 16 Мегабайт");
            return;
        }
        AnimePicture animePicture = new AnimePicture(new FileInputStream(selectedFile),selectedFile.getName() ,ImageType.FRAME);
        Button button = new Button();
        button.setOnAction(event1 -> {
            fileList.remove(animePicture);
        });
        button.setText("Удалить");
        animePicture.setButton(button);
        fileList.add(animePicture);
        System.out.println(selectedFile.getName());

    }

    @FXML
    public void addPoster(ActionEvent event) throws FileNotFoundException {
        if (fileList.stream().anyMatch(a -> a.getType().equals(ImageType.POSTER))){
            errorText.setText("Постер уже выбран. Удалите старый.");
            return;
        }
        errorText.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open poster image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null){
            errorText.setText("Неверный файл");
            return;
        }
        if (selectedFile.length() >= 16777216 - 100){
            errorText.setText("Файл не может быть больше 16 Мегабайт");
            return;
        }

        AnimePicture animePicture = new AnimePicture(new FileInputStream(selectedFile),selectedFile.getName() ,ImageType.POSTER);
        Button button = new Button();
        button.setOnAction(event1 -> {
            fileList.remove(animePicture);
        });
        button.setText("Удалить");
        animePicture.setButton(button);
        fileList.add(animePicture);
        System.out.println(selectedFile.getName());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filesTable.setPlaceholder(new Label("Вы не выбрали файл"));
        fileNameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFileName()));
        deleteColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getButton()));
        typeColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getType().toString()));
        filesTable.setItems(fileList);
    }
}
