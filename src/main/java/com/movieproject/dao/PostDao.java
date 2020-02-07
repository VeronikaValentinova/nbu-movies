package com.movieproject.dao;

import com.movieproject.bean.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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

        MapSqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("Title", m.getTitle())
                .addValue("category", m.getCategory())
                .addValue("director", m.getDirector())
                .addValue("music", m.getMusic())
                .addValue("dateOfCreation", m.getDateOfCreation())
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


}
