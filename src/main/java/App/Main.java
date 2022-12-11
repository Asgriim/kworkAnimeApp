package App;

import data.Permissions;
import data.User;
import database.DatabaseManager;
import database.MySqlManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class Main extends Application {

    private static DatabaseManager databaseManager;
    private static User user;
    @Override
    public void start(Stage stage) throws Exception {
        user = new User(0,"","", Permissions.GUEST);
        Logger.getLogger("com.zaxxer.hikari.pool.PoolBase").setLevel(Level.OFF);
        Logger.getLogger("com.zaxxer.hikari.pool.HikariPool").setLevel(Level.OFF);
        Logger.getLogger("com.zaxxer.hikari.HikariDataSource").setLevel(Level.OFF);
        Logger.getLogger("com.zaxxer.hikari.HikariConfig").setLevel(Level.OFF);
        Logger.getLogger("com.zaxxer.hikari.util.DriverDataSource").setLevel(Level.OFF);
        databaseManager = MySqlManager.getInstance();
        if (!databaseManager.configureDb()){
            System.out.println("Не удалось подключится к серверу");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
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

    public static User getUser() {
        return user;
    }
}
