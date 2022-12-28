package database;

import data.*;

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
    String addAnime(User user, Anime anime, AnimePicture poster, List<AnimePicture> frames) throws SQLException;
    Anime getAnimeById(int id) throws SQLException;
    boolean removeAnime(User user, Anime anime) throws SQLException;
    boolean removeUserAnime(User user, Anime anime) throws SQLException;
    List<Anime> getUserAnimes(User user, AnimeStatus status) throws SQLException;
    AnimeStatus getUserAnimeStatus(User user, Anime anime) throws SQLException;
    List<AnimePicture> getAnimePictures(Anime anime) throws SQLException;
    void gradeAnime(User user, Anime anime, int grade) throws SQLException;
    int getUserAnimeGrade(User user, Anime anime) throws SQLException;
    double getAnimeAvgGrade(Anime anime) throws SQLException;
    List<AnimeReview> getAnimeReviews(Anime anime) throws SQLException;
    void deleteReview(AnimeReview animeReview, User user) throws SQLException;
    void addAnimeReview(User user, Anime anime, String comment) throws SQLException;
    void adminLog(User user, Anime anime, String action) throws SQLException;



}
