package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import data.Anime;
import data.AnimeStatus;
import data.User;

import java.util.List;

public class MySqlManager implements DatabaseManager{
    private HikariDataSource dataSource;

    public MySqlManager() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://db4free.net:3306/anime_app_test");
        config.setUsername("anime_test123");
        config.setPassword("um6yrdfty?");
        dataSource = new HikariDataSource(config);
    }

    @Override
    public boolean configureDb() {
        return false;
    }

    @Override
    public List<Anime> getAnimeList() {
//        List<Anime>
        return null;
    }

    @Override
    public String registerUser(String login, String passwd) {
        return null;
    }

    @Override
    public String logIn(String login, String passwd) {
        return null;
    }

    @Override
    public String addToWatching(User user, Anime anime) {
        return null;
    }

    @Override
    public String addToWatched(User user, Anime anime) {
        return null;
    }

    @Override
    public String addToWillWatch(User user, Anime anime) {
        return null;
    }

    @Override
    public String addAnime(User user, Anime anime) {
        return null;
    }

    @Override
    public String removeAnime(User user, Anime anime) {
        return null;
    }

    @Override
    public List<Anime> getUserAnimes(User user, AnimeStatus status) {
        return null;
    }
}
