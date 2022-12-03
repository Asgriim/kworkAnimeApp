package App;

import database.DatabaseManager;
import database.MySqlManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main extends Application {

    private static DatabaseManager databaseManager;
    @Override
    public void start(Stage stage) throws Exception {
        databaseManager = MySqlManager.getInstance();
        if (!databaseManager.configureDb()){
            System.out.println("umer");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
//        System.out.println("bebra");
    }

    public static String getHash(String s){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            byte[] bytes = digest.digest(s.getBytes(StandardCharsets.UTF_8));
            BigInteger bigInteger = new BigInteger(1,bytes);
            return bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
