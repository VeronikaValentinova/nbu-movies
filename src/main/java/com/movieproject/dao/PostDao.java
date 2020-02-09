package com.movieproject.dao;

import com.movieproject.bean.Actor;
import com.movieproject.bean.Event;
import com.movieproject.bean.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.text.ParseException;

@Repository
public class PostDao {

    @Autowired
    DataSource dataSource;

//    @PostConstruct
//    private void initialize(){ setDataSource(dataSource); }

    private final NamedParameterJdbcOperations namedTemplate;

    public PostDao(NamedParameterJdbcOperations namedTemplate) {
        this.namedTemplate = namedTemplate;
    }

    public int addMovie(Movie m) {
        final String sql = "INSERT INTO movies " +
                "(Title, category, director, music, dateOfCreation, studio, " +
                "Description, rating, trailer, language, country, imdbrating, poster, countryOfShooting, duration)" +
                "values (:Title, :category, :director, :music, :dateOfCreation, :studio, " +
                ":Description, :rating, :trailer, :language, :country, :imdbrating, :poster, :countryOfShooting, :duration)";

        String date = m.getDateOfCreation();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dt = null;
        try {
            dt = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        String currentTime = sdf.format(dt);

        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("Title", m.getTitle())
                .addValue("category", m.getCategory())
                .addValue("director", m.getDirector())
                .addValue("music", m.getMusic())
                .addValue("dateOfCreation", currentTime)
                .addValue("studio", m.getStudio())
                .addValue("Description", m.getDescription())
                .addValue("rating", m.getRating())
                .addValue("trailer", m.getTrailer())
                .addValue("language", m.getLanguage())
                .addValue("country", m.getCountry())
                .addValue("imdbrating", m.getImdbRating())
                .addValue("poster", m.getPoster())
                .addValue("countryOfShooting", m.getCountryOfShooting())
                .addValue("duration", m.getDuration());

        return namedTemplate.update(sql, paramMap);
    }

    public void addMovieToWishList(Integer user_id, Integer movie_id) {
        //@formatter:off
        final String sql = "INSERT INTO  wished_movies (user_id, movie_id)           "
                + " SELECT :user_id, movie_id FROM movies WHERE movie_id = :movie_id ";
        //@formatter:on

        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("user_id", user_id)
                .addValue("movie_id", movie_id);
        namedTemplate.update(sql, paramMap);
    }

    public void saveMovieComment(Integer user_id, String title, String comment, String date) {
        //@formatter:off
        final String sql = "INSERT INTO  movie_comments (user_id, movie_id, comment, date)            "
                + " SELECT :user_id, movie_id, :comment, :date FROM movies WHERE title = :title ";
        //@formatter:on
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("user_id", user_id)
                .addValue("title", title)
                .addValue("comment", comment)
                .addValue("date", date);
        namedTemplate.update(sql, paramMap);
    }

    public void saveUser(String email, String pass, String username) {
        //@formatter:off
        final String sql = "INSERT INTO  user (username, email, password) "
                + " values (:username, :email, :pass);";
        //@formatter:on
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("email", email)
                .addValue("pass", pass);
        namedTemplate.update(sql, paramMap);
    }

    public int deleteEvent(Integer event_id) {
        //@formatter:off
        final String sql = "DELETE FROM events WHERE event_id = :event_id ";
        //@formatter:on
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("event_id", event_id);
        return namedTemplate.update(sql, paramMap);
    }

    public void addMovieToWatchedList(Integer user_id, Integer movie_id) {
        //@formatter:off
        final String sql = "INSERT INTO  watched_movies (user_id, movie_id)          "
                + " SELECT :user_id, movie_id FROM movies WHERE movie_id = :movie_id ";
        //@formatter:on

        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("user_id", user_id)
                .addValue("movie_id", movie_id);
        namedTemplate.update(sql, paramMap);
    }

    public int setUserRole(Integer user_id) {
        Integer roleId = 2; //role USER = 2, role ADMIN = 1 !
        final String sql = "INSERT INTO user_role (user_id, role_id) " +
                "VALUES(:user_id, :role_id)";

        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("user_id", user_id)
                .addValue("role_id", roleId);
        return namedTemplate.update(sql, paramMap);
    }


    public int removeMovieFromWishList(Integer movie_id) {
        final String sql = "DELETE FROM wished_movies WHERE movie_id = :movie_id ";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("movie_id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int removeMovieFromWatchedList(Integer movie_id) {
        final String sql = "DELETE FROM watched_movies WHERE movie_id = :movie_id ";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("movie_id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int addMovieAward(String award, Integer movieId) {
        final String sql = "INSERT INTO  movie_awards (award, movie_id)           "
                + " VALUES (:award, :movie_id) ";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("award", award)
                .addValue("movie_id", movieId);
        return namedTemplate.update(sql, paramMap);
    }

    public int addMovieMainActor(String actor, Integer movie_id) {
        final String sql = "INSERT INTO  movie_mainactors (actor, movie_id)           "
                + " VALUES (:actor, :movie_id) ";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("actor", actor)
                .addValue("movie_id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int addMovieKeyword(String keyword, Integer movie_id) {
        final String sql = "INSERT INTO  movie_keywords (keyword, movie_id)           "
                + " VALUES (:keyword, :movie_id) ";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("keyword", keyword)
                .addValue("movie_id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    //MAYBE not needed

//    public int addMovieComment(Comment comment, Integer movie_id) {
//        final String sql = "INSERT INTO  movie_comments (comment, movie_id, user_id, date)           "
//                + " VALUES (:comment, :movie_id, :user_id, :date) ";
//        MapSqlParameterSource paramMap = new MapSqlParameterSource()
//                .addValue("comment", comment.getComment())
//                .addValue("movie_id", movie_id)
//                .addValue("user_id", comment.getUser_id())
//                .addValue("date", comment.getDate());
//        return namedTemplate.update(sql, paramMap);
//    }

    public int addMovieActor(Actor actor, Integer movie_id) {
        final String sql = "INSERT INTO  movie_actors (realName, movie_id, roleName)           "
                + " VALUES (:realName, :movie_id, :roleName) ";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("realName", actor.getRealName())
                .addValue("movie_id", movie_id)
                .addValue("roleName", actor.getRoleName());
        return namedTemplate.update(sql, paramMap);
    }

    public int changePassword(Integer userId, String password) {
        final String sql = "UPDATE user SET password = :password WHERE id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("password", password)
                .addValue("id", userId);
        return namedTemplate.update(sql, paramMap);
    }

    public int archiveOrRestoreUser(Integer userId, Integer param) {
        final String sql = "UPDATE user SET archived = :archived WHERE id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("archived", param)
                .addValue("id", userId);
        return namedTemplate.update(sql, paramMap);
    }

    public int archiveOrRestoreCategory(String category, Integer param) {
        final String sql = "UPDATE category SET archived = :archived WHERE category = :category";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("archived", param)
                .addValue("category", category);
        return namedTemplate.update(sql, paramMap);
    }

    public int archiveOrRestoreMovie(Integer movie_id, Integer param) {
        final String sql = "UPDATE movies SET archived = :archived WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("archived", param)
                .addValue("id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int rateMovie(Integer movie_id, Integer rating) {
        final String sql = "INSERT INTO movie_ratings (movie_id, rating) VALUES (:movie_id, :rating)";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("movie_id", movie_id)
                .addValue("rating", rating);
        return namedTemplate.update(sql, paramMap);
    }

    public int rateUser(Integer user_id, Integer rating) {
        final String sql = "INSERT INTO user_ratings (user_id, rating) VALUES (:user_id, :rating)";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("user_id", user_id)
                .addValue("rating", rating);
        return namedTemplate.update(sql, paramMap);
    }

    public int updateAverageMovieRating(Integer avg, Integer movie_id) {
        final String sql = "UPDATE movies SET rating = :rating WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id)
                .addValue("rating", avg);
        return namedTemplate.update(sql, paramMap);
    }

    public int updateAverageUserRating(Integer avg, Integer user_id) {
        final String sql = "UPDATE user SET userRating = :rating WHERE id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", user_id)
                .addValue("rating", avg);
        return namedTemplate.update(sql, paramMap);
    }

    public int addEvent(Event event, Integer userId) {
        final String sql = "INSERT INTO events (movie_id, user_id, date, place) VALUES (:movie_id, :user_id, :date, :place)";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("movie_id", event.getMovie_id())
                .addValue("user_id", userId)
                .addValue("date", event.getDate())
                .addValue("place", event.getPlace());
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteMovie(Integer movie_id) {
        final String sql = "DELETE FROM movies WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteComments(Integer movie_id) {
        final String sql = "DELETE FROM movie_comments WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteKeywords(Integer movie_id) {
        final String sql = "DELETE FROM movie_keywords WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteActors(Integer movie_id) {
        final String sql = "DELETE FROM movie_actors WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteAwards(Integer movie_id) {
        final String sql = "DELETE FROM movie_awards WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteMainActors(Integer movie_id) {
        final String sql = "DELETE FROM movie_mainactors WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteRatings(Integer movie_id) {
        final String sql = "DELETE FROM movie_ratings WHERE movie_id = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteActor(Integer movie_id, Integer actor_id) {
        final String sql = "DELETE FROM movie_actors WHERE movie_id = :id AND id = :actorId";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id)
                .addValue("actorId", actor_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int deleteMainActor(Integer movie_id, Integer actor_id) {
        final String sql = "DELETE FROM movie_mainactors WHERE movie_id = :id AND id = :actorId";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", movie_id)
                .addValue("actorId", actor_id);
        return namedTemplate.update(sql, paramMap);
    }

    public int addActor(Actor actor) {
        final String sql = "INSERT INTO movie_actors (realName, movie_id, roleName) VALUES (:realName, :movie_id, :roleName)";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("realName", actor.getRealName())
                .addValue("movie_id", actor.getMovie_id())
                .addValue("roleName", actor.getRoleName());
        return namedTemplate.update(sql, paramMap);
    }
}
