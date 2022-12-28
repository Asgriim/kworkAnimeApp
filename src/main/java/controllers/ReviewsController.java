package controllers;

import App.Main;
import data.*;
import database.DatabaseManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReviewsController implements Initializable {

    @FXML
    private TableColumn<?, ?> gradeColumn;

    @FXML
    private Text headText;

    @FXML
    private TableView<AnimeReview> reviewTable;

    @FXML
    private TableColumn<AnimeReview, VBox> textColumn;

    @FXML
    private TableColumn<AnimeReview, String> userNameColumn;

    private DatabaseManager databaseManager;

    private ObservableList<AnimeReview> reviewsList;

    private User user;

    private Anime currAnime;

    public void setCurrAnime(Anime currAnime) {
        this.currAnime = currAnime;
        try {
            reviewsList.addAll(databaseManager.getAnimeReviews(currAnime));
            headText.setText("Количество отзывов: " + reviewsList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReviewsController() {
        this.databaseManager = Main.getDatabaseManager();
        this.user = Main.getUser();
        this.reviewsList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reviewTable.setPlaceholder(new Label("Отзывов пока нет"));
        textColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(createVbox(data.getValue(),user)));
//        userNameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getUserName()));
        reviewTable.setItems(reviewsList);
    }


//    public Label createLabel(String s){
//        Label label = new Label();
//        label.;
//        label.setWrapText(true);
//        label.setAlignment(Pos.CENTER);
//        label.set
////        label.setPrefHeight(80);
//        label.setText(s);
//        return label;
//    }
//
    public Text createText(String s){
        Text text = new Text();
        text.setStyle("-fx-fill: white");

        text.setTextAlignment(TextAlignment.CENTER);
        text.setText(s);
        return text;
    }

    public VBox createVbox(AnimeReview animeReview, User user){
        VBox vBox = new VBox();
        Text label = createText(animeReview.getUserName());
        label.setFont(new Font(16));
        Text label1 = createText(animeReview.getComment());
        vBox.setSpacing(5);
        label.setWrappingWidth(590);
        label1.setWrappingWidth(590);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(label);
        vBox.getChildren().add(label1);
        vBox.setPrefWidth(600);
//        vBox.setPrefHeight(100);
        vBox.setStyle("-fx-background-color: #1b1b1b");
        if (user.getPermission().equals(Permissions.ADMIN) ||
                (user.getPermission().equals(Permissions.USER) && animeReview.getUserId() == user.getId())){
            vBox.getChildren().add(createButton(animeReview));
        }
        return vBox;
    }


    Button createButton(AnimeReview animeReview){
        Button button = new Button();
        button.setText("удалить");
        button.setOnAction(event -> {
            try {
                databaseManager.deleteReview(animeReview,user);
                reviewsList.remove(animeReview);
                headText.setText("Количество отзывов: " + reviewsList.size());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return button;
    }
}
