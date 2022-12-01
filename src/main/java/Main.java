import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 401);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println("git pidor blyat");

        launch();
//        System.out.println("bebra");
    }
}
