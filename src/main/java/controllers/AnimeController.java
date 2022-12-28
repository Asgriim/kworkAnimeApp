package controllers;

import App.Main;
import data.*;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AnimeController implements Initializable {

    @FXML
    public Text statusText;

    private final String statusTextPref = "Текущий статус: ";
    @FXML
    public ImageView posterView;
    @FXML
    public Pagination paginator;
    @FXML
    public HBox imgHbox;
    @FXML
    public Text avgGrade;
    @FXML
    public Button setGradeButton;

    @FXML
    public Slider gradeSlider;
    @FXML
    public Button writeReviewButton;
    @FXML
    public Text avgGrade1;
    @FXML
    public Button openReviewsButton;

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

    private List<Image> imageList;

    private User user;

    private AnimePicture poster;

    private List<AnimePicture> frames;

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
            AnimeStatus status = databaseManager.getUserAnimeStatus(user,tempAnime);
            if (status != null){
                if (status.equals(AnimeStatus.WATCHED)){
                    statusText.setText(statusTextPref + "просмотрено");
                }
                if(status.equals(AnimeStatus.WILL_WATCH)){
                    statusText.setText(statusTextPref + "буду смотреть");
                }
                if(status.equals(AnimeStatus.WATCHING)){
                    statusText.setText(statusTextPref + "смотрю");
                }
            }
            else {
                statusText.setText(statusTextPref + "нет");
            }
            double avgGr = databaseManager.getAnimeAvgGrade(tempAnime);
            int grade = databaseManager.getUserAnimeGrade(user,tempAnime);
            if (avgGr == 0){
                avgGrade.setText("Общая оценка: нет");
            }
            else
                avgGrade.setText("Общая оценка: " + avgGr + "/5");
            if (grade == 0){
                avgGrade1.setText("Ваша оценка: нет");
            }
            else
                avgGrade1.setText("Ваша оценка: " + grade + "/5");
            frames = databaseManager.getAnimePictures(tempAnime);
            AnimePicture poster = frames.stream().filter(a -> a.getType().equals(ImageType.POSTER)).findAny().orElse(null);
            if (poster == null){
                return;
            }
            Image image = new Image(poster.getFile());
            posterView.setImage(image);
            frames.remove(poster);
            if (frames.size() <= 3){
                paginator.setMaxPageIndicatorCount(frames.size());
                paginator.setPageCount(frames.size());
            }
            imageList = createImageList();
            paginator.setPageCount(frames.size());
            paginator.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer integer) {
                    return createPage(integer);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
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
            statusText.setText(statusTextPref + "просмотрено");
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
            statusText.setText(statusTextPref + "смотрю");
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
            statusText.setText(statusTextPref + "буду смотреть");
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
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public HBox createPage(int index) {
        ImageView imageView = new ImageView();
        Image image = imageList.get(index);
        imageView.setImage(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(800);
//        imageView.setSmooth(true);
//        imageView.setCache(true);

        imageView.setPreserveRatio(true);
        HBox pageBox = new HBox();
        pageBox.setAlignment(Pos.CENTER);
        pageBox.getChildren().add(imageView);
        return pageBox;
    }


    List<Image> createImageList(){
        return frames.stream().map(a -> new Image(a.getFile())).collect(Collectors.toList());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    @FXML
    public void gradeAction(ActionEvent event) {
        if (user.getPermission().equals(Permissions.GUEST)){
            openErrorPage();
            return;
        }
        int grade = (int) gradeSlider.getValue();

        avgGrade1.setText("Ваша оценка: " + grade + "/5");
        try {
            databaseManager.gradeAnime(user,tempAnime,grade);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double avgGr = 0;
        try {
            avgGr = databaseManager.getAnimeAvgGrade(tempAnime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (avgGr == 0){
            avgGrade.setText("Общая оценка: нет");
        }
        else
            avgGrade.setText("Общая оценка: " + avgGr + "/5");
    }


    @FXML
    public void writeReviewAction(ActionEvent event) {
        if (user.getPermission().equals(Permissions.GUEST)){
            openErrorPage();
            return;
        }
        Stage stage = new Stage();
        Scene scene = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../view/addReview.fxml"));

        try {
            fxmlLoader.load();
            Parent root =  fxmlLoader.getRoot();
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(root));
            stage1.setResizable(false);
            AddReviewController controller = fxmlLoader.getController();
            controller.setCurrAnime(tempAnime);
            stage1.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void openReviews(ActionEvent event) {
        Stage stage = new Stage();
        Scene scene = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../view/reviews.fxml"));

        try {
            fxmlLoader.load();
            Parent root =  fxmlLoader.getRoot();
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(root));
            stage1.setResizable(false);
            ReviewsController controller = fxmlLoader.getController();
            controller.setCurrAnime(tempAnime);
            stage1.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
