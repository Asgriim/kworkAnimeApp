package database;

import data.Anime;
import data.AnimeStatus;
import data.User;

import java.util.List;

public interface DatabaseManager {
    boolean configureDb();
    List<Anime> getAnimeList();
    String registerUser(String login,String passwd);
    String logIn(String login,String passwd);
    String addToWatching(User user,Anime anime);
    String addToWatched(User user,Anime anime);
    String addToWillWatch(User user,Anime anime);
    String addAnime(User user,Anime anime);
    String removeAnime(User user,Anime anime);
    List<Anime> getUserAnimes(User user, AnimeStatus status);

}
