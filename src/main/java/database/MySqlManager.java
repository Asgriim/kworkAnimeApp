package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import data.Anime;
import data.AnimeStatus;
import data.Permissions;
import data.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlManager implements DatabaseManager{
    private final HikariDataSource dataSource;
    private static DatabaseManager instance;
//    ---------------------------------------------
//      Используется библиотека HikariCP для управления пулом подключений к базе.
//    ---------------------------------------------
    private MySqlManager(String jdbcURL, String username, String password) {
        HikariConfig config = new HikariConfig(); // заполняем конфиг для хикари
        config.setJdbcUrl(jdbcURL); //ссылка на базу данных
        config.setUsername(username); // логин к базе
        config.setPassword(password);// пароль к базе
        config.setMaximumPoolSize(7);
        config.addDataSourceProperty("cachePrepStmts", "true"); // кешировать  prepareStatement
        config.addDataSourceProperty("prepStmtCacheSize", "250"); // макс количество кешированных prepareStatement
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); // макс длина prepareStatement, которое будет закешированно
        dataSource = new HikariDataSource(config);
    }

    public static DatabaseManager getInstance() {
        if (instance == null){
//            instance = new MySqlManager("jdbc:mysql://remotemysql.com:3306/BlaLdACAvt", // бесплатная MySQL база данных
//                    "BlaLdACAvt", // логин
//                    "PCEYHiRpnt"// пароль
//            );
            instance = new MySqlManager("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7586253",
                    "sql7586253",
                    "L9cl6cV3E9");
        }
        return instance;
    }

    @Override
    public boolean configureDb() {
        try (Connection connection =dataSource.getConnection();
             Statement statement =connection.createStatement()){
            //создать таблицу аниме, если её ещё нет
            boolean out = statement.execute(
                    "CREATE TABLE IF NOT EXISTS Animes(" +
                    "    Id INT AUTO_INCREMENT PRIMARY KEY," +
                    "    Name TEXT NOT NULL," +
                    "    Description TEXT NOT NULL" + ")"

            );
            // создать таблицу юзеров
            out = statement.execute("create table if not exists Users(" +
                    "    Id INT AUTO_INCREMENT PRIMARY KEY," +
                    "    Login TEXT NOT NULL," +
                    "    Password TEXT," +
                    "    Permission ENUM('USER','ADMIN')" +
                    ");");
            // создать таблицу аниме для админа (буду смотреть/смотрю/просмотрел)
            out = statement.execute("CREATE TABLE IF NOT EXISTS USERS_ANIMES(" +
                    "    UserId INT," +
                    "    AnimeId INT," +
                    "    AnimeName TEXT," +
                    "    Status ENUM('WATCHING','WILL_WATCH','WATCHED')," +
                    "    FOREIGN KEY (UserId) REFERENCES Users (Id) ON DELETE CASCADE," +
                    "    FOREIGN KEY (AnimeId) REFERENCES Animes (Id) ON DELETE CASCADE" +
                    "    );");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }



    @Override
    public List<Anime> getAnimeList() throws SQLException {
        List<Anime> animeList = new ArrayList<>();
        try (Connection connection =dataSource.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Animes");
            while (resultSet.next()){
                Anime anime = new Anime(resultSet.getInt("Id"),resultSet.getString("Name"),resultSet.getString("Description"));
                animeList.add(anime);
            }
            return animeList;
        }
    }

    private boolean checkIfLoginExist(String login) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE Login=?")){
            statement.setString(1,login);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        }
    };

    @Override
    public String registerUser(String login, String passwd) throws SQLException {
        if (checkIfLoginExist(login)){
            return "exist";
        }
        try (Connection connection =dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Users (login, password, permission) VALUES (?,?,?);")){
            statement.setString(1,login);
            statement.setString(2,passwd);
            statement.setString(3, Permissions.USER.toString());
            statement.execute();
            User user = selectUser(login, passwd);
//            createUserAnimeTable(user);
            return "register";
        }
    }

    private User selectUser(String login, String passwd) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE Login=? and Password=?")){
            statement.setString(1,login);
            statement.setString(2,passwd);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()){
                return null;
            }
            return new User(resultSet.getInt("Id"), login,passwd,Permissions.valueOf(resultSet.getString("Permission")));
        }
    }

//    private void createUserAnimeTable(User user) throws SQLException {
//        try (Connection connection =dataSource.getConnection();
//             Statement statement =connection.createStatement()){
//            statement.execute("CREATE TABLE IF NOT EXISTS "+ user.getAnimeTableName() +"(" +
//                    "  AnimeId INT," +
//                    "  Name TEXT NOT NULL," +
//                    "  Status ENUM('WATCHING','WILL_WATCH','WATCHED')" +
//                    ")");
//        }
//    }

    @Override
    public User logIn(String login, String passwd) throws SQLException {
        if (!checkIfLoginExist(login)){
            return null;
        }
       return selectUser(login,passwd);
    }

    @Override
    public String addToWatching(User user, Anime anime) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO USERS_ANIMES (UserId, AnimeId, AnimeName, Status) VALUES (?,?,?,?)")){
            if (isAnimeExistInUserTable(user,anime)){
                removeUserAnime(user, anime);
            }
            statement.setInt(1,user.getId());
            statement.setInt(2,anime.getId());
            statement.setString(3,anime.getName());
            statement.setString(4,AnimeStatus.WATCHING.toString());
            statement.execute();

        }
        return "ok";
    }

    @Override
    public String addToWatched(User user, Anime anime) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO USERS_ANIMES (UserId, AnimeId, AnimeName, Status) VALUES (?,?,?,?)")){
            if (isAnimeExistInUserTable(user,anime)){
                removeUserAnime(user, anime);
            }
            statement.setInt(1,user.getId());
            statement.setInt(2,anime.getId());
            statement.setString(3,anime.getName());
            statement.setString(4,AnimeStatus.WATCHED.toString());
            statement.execute();
        }
        return "ok";
    }

    @Override
    public String addToWillWatch(User user, Anime anime) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO USERS_ANIMES (UserId, AnimeId, AnimeName, Status) VALUES (?,?,?,?)")){
            if (isAnimeExistInUserTable(user,anime)){
                removeUserAnime(user, anime);
            }
            statement.setInt(1,user.getId());
            statement.setInt(2,anime.getId());
            statement.setString(3,anime.getName());
            statement.setString(4,AnimeStatus.WILL_WATCH.toString());
            statement.execute();
        }
        return "ok";
    }

    @Override
    public String addAnime(User user, Anime anime) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Animes(Name,Description) VALUES (?,?)")){
            statement.setString(1,anime.getName());
            statement.setString(2,anime.getDescription());
            statement.execute();

        }
        return "ok";
    }

    @Override
    public Anime getAnimeById(int id) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Animes WHERE Id =?")){
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();

//            ResultSet resultSet = statement.executeQuery("SELECT * FROM Animes WHERE Id =" + id);
            if (!resultSet.next()){
                return null;
            }
            return new Anime(id,resultSet.getString("Name"),resultSet.getString("Description"));
        }
    }

    @Override
    public boolean removeAnime(User user, Anime anime) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             Statement statement = connection.createStatement()){
            statement.execute("DELETE FROM Animes WHERE Id=" + anime.getId());
        }
        return true;
    }

    @Override
    public boolean removeUserAnime(User user, Anime anime) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM USERS_ANIMES where AnimeId=? and UserId=?")){
            statement.setInt(1,anime.getId());
            statement.setInt(2,user.getId());
            statement.execute();
//            statement.execute("DELETE FROM "+ user.getAnimeTableName() +" where AnimeId =" + anime.getId());
        }
        return true;
    }

    @Override
    public List<Anime> getUserAnimes(User user, AnimeStatus status) throws SQLException {
        List<Anime> userAnimeList = new ArrayList<>();
        try (Connection connection =dataSource.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS_ANIMES WHERE UserId="+ user.getId() + " AND Status='"+ status + "'");
            while (resultSet.next()){
                Anime anime = new Anime(resultSet.getInt("AnimeId"),resultSet.getString("AnimeName"),null);
                userAnimeList.add(anime);
            }
        }
        return userAnimeList;
    }

    @Override
    public AnimeStatus getUserAnimeStatus(User user, Anime anime) throws SQLException {
        if (!isAnimeExistInUserTable(user, anime)){
            return null;
        }
        try (Connection connection =dataSource.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS_ANIMES WHERE UserId="+ user.getId() + " AND AnimeId=" + anime.getId());
            if (!resultSet.next()){
                return null;
            }
            return AnimeStatus.valueOf(resultSet.getString("Status"));
        }

    }


    private boolean isAnimeExistInUserTable(User user,Anime anime) throws SQLException {
        try (Connection connection =dataSource.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM USERS_ANIMES WHERE AnimeId=" + anime.getId() + " and UserId=" + user.getId());
            resultSet.next();
            return  resultSet.getInt(1) > 0;
        }
    }
}
