package database;

import data.Anime;
import data.AnimeStatus;
import data.User;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseManager {
    boolean configureDb();
    List<Anime> getAnimeList() throws SQLException;
    String registerUser(String login,String passwd) throws SQLException;
    User logIn(String login, String passwd) throws SQLException;
    String addToWatching(User user,Anime anime) throws SQLException;
    String addToWatched(User user,Anime anime) throws SQLException;
    String addToWillWatch(User user,Anime anime) throws SQLException;
    String addAnime(User user,Anime anime) throws SQLException;
    Anime getAnimeById(int id) throws SQLException;
    boolean removeAnime(User user, Anime anime) throws SQLException;
    boolean removeUserAnime(User user, Anime anime) throws SQLException;
    List<Anime> getUserAnimes(User user, AnimeStatus status) throws SQLException;
    AnimeStatus getUserAnimeStatus(User user, Anime anime) throws SQLException;
}
