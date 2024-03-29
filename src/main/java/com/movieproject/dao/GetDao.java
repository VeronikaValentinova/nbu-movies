package com.movieproject.dao;

import com.movieproject.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class GetDao extends JdbcDaoSupport {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }
    private final NamedParameterJdbcOperations namedTemplate;

    public GetDao(NamedParameterJdbcOperations namedTemplate) {
        this.namedTemplate = namedTemplate;
    }

    public List<User> getUserRole(String email, String pass){
        //@formatter:off
        String sql = "SELECT r.role, u.id, u.username    " +
                     "  FROM role r, user u, user_role ur " +
                     " WHERE 1 = 1                        " +
                     "   AND r.role_id = ur.role_id       " +
                     "   AND u.id = ur.user_id            " +
                     "   AND u.password = :pass           " +
                     "   AND u.email = :email             ";
        //@formatter:on

        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("pass", pass);

        return namedTemplate.query(sql, sp, (r, i)-> {
            User u = new User();
            u.setId(r.getInt("id"));
            u.setRole(r.getString("role"));
            u.setName(r.getString("username"));
            return u;
        });
    }

    public List<User> getUserRoleByUsername(String username, String pass){
        //@formatter:off
        String sql = "SELECT r.role, u.id, u.username    " +
                "  FROM role r, user u, user_role ur " +
                " WHERE 1 = 1                        " +
                "   AND r.role_id = ur.role_id       " +
                "   AND u.id = ur.user_id            " +
                "   AND u.password = :pass           " +
                "   AND u.username = :username             ";
        //@formatter:on

        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("pass", pass);

        return namedTemplate.query(sql, sp, (r, i)-> {
            User u = new User();
            u.setId(r.getInt("id"));
            u.setRole(r.getString("role"));
            u.setName(r.getString("username"));
            return u;
        });
    }

    public List<Movie> getAllMovies() {
        //@formatter:off
        String sql = "SELECT movie_id          " +
                     "     , category          " +
                     "     , director          " +
                     "     , music             " +
                     "     , title             " +
                     "     , dateOfCreation    " +
                     "     , studio            " +
                     "     , description       " +
                     "     , rating            " +
                     "     , poster            " +
                     "     , trailer           " +
                     "     , country           " +
                     "     , imdbrating        " +
                     "     , language          " +
                     "     , countryOfShooting " +
                     "     , duration          " +
                     "  FROM movies            " +
                      "ORDER BY dateOfCreation DESC";
        //@formatter:on

        return namedTemplate.query(sql, new MapSqlParameterSource(), (r, i) -> {
            Movie m = new Movie();
            m.setMovie_id(r.getInt("movie_id"));
            m.setCategory(r.getString("category"));
            m.setDirector(r.getString("director"));
            m.setMusic(r.getString("music"));
            m.setTitle(r.getString("title"));
            m.setDateOfCreation(r.getString("dateOfCreation"));
            m.setStudio(r.getString("studio"));
            m.setDescription(r.getString("description"));
            m.setRating(r.getString("rating"));
            m.setPoster(r.getString("poster"));
            m.setTrailer(r.getString("trailer"));
            m.setCountry(r.getString("country"));
            m.setImdbRating(r.getInt("imdbrating"));
            m.setLanguage(r.getString("language"));
            m.setLanguage(r.getString("countryOfShooting"));
            m.setLanguage(r.getString("duration"));
            return m;
        });
    }

    public List<Actor> getMovieActors(String title) {
        //@formatter:off
        String sql = "SELECT ma.realName, ma.roleName                  " +
                     "  FROM movie_actors ma, movies m " +
                     " WHERE 1 = 1                     " +
                     "   AND m.movie_id = ma.movie_id  " +
                     "   AND m.title = :title          ";
        //@formatter:on

        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("title", title);
        return namedTemplate.query(sql, sp, (r, i) -> {
            Actor actor = new Actor();
            actor.setRealName(r.getString("realName"));
            actor.setRoleName(r.getString("roleName"));
            return actor;
        });
    }

    public List<String> getMovieMainActors(String title) {
        //@formatter:off
        String sql = "SELECT ma.actor                      " +
                     "  FROM movie_mainactors ma, movies m " +
                     " WHERE 1 = 1                         " +
                     "   AND m.movie_id = ma.movie_id      " +
                     "   AND m.title = :title              ";
        //@formatter:on

        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("title", title);
        return namedTemplate.query(sql, sp, (r, i) -> r.getString("actor"));
    }

    public List<String> getMovieAwards(String title) {
        //@formatter:off
        String sql = "SELECT ma.award                  " +
                     "  FROM movie_awards ma, movies m " +
                     " WHERE 1 = 1                     " +
                     "   AND m.movie_id = ma.movie_id  " +
                     "   AND m.title = :title          ";
        //@formatter:on
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("title", title);
        return namedTemplate.query(sql, sp, (r, i) -> r.getString("award"));
    }

    public List<Movie> getUserWishList(Integer userId) {
        //@formatter:off
        String sql = " SELECT m.movie_id, m.title, m.category,     " +
                "        m.director, m.music,                      " +
                "        m.dateOfCreation, m.studio,               " +
                "        m.description, m.rating,                  " +
                "        m.trailer, m.language, m.country,         " +
                "        m.poster, m.countryOfShooting, m.duration " +
                "  FROM movies m, wished_movies w                  " +
                " WHERE 1 = 1                                      " +
                "   AND m.movie_id = w.movie_id                    " +
                "   AND user_id = :userId                          ";
        //@formatter:on
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("userId", userId);
        return namedTemplate.query(sql, sp, (r, i) -> {
            Movie m = new Movie();
            m.setMovie_id(r.getInt("movie_id"));
            m.setTitle(r.getString("title"));
            m.setCategory(r.getString("category"));
            m.setDirector(r.getString("director"));
            m.setMusic(r.getString("music"));
            m.setDateOfCreation(r.getString("dateOfCreation"));
            m.setStudio(r.getString("studio"));
            m.setDescription(r.getString("description"));
            m.setRating(r.getString("rating"));
            m.setTrailer(r.getString("trailer"));
            m.setLanguage(r.getString("language"));
            m.setCountry(r.getString("country"));
            m.setPoster(r.getString("poster"));
            m.setCountryOfShooting(r.getString("countryOfShooting"));
            m.setDuration(r.getString("duration"));
            return m;
        });
    }

    public List<Movie> getUserWatchedList(Integer userId) {
        //@formatter:off
        String sql = " SELECT m.movie_id, m.title, m.category,          " +
                     "        m.director, m.music,                      " +
                     "        m.dateOfCreation, m.studio,               " +
                     "        m.description, m.rating,                  " +
                     "        m.trailer, m.language, m.country,         " +
                     "        m.poster, m.countryOfShooting, m.duration " +
                     "  FROM movies m, watched_movies w                 " +
                     " WHERE 1 = 1                                      " +
                     "   AND m.movie_id = w.movie_id                    " +
                     "   AND user_id = :userId                          ";
        //@formatter:on
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("userId", userId);
        return namedTemplate.query(sql, sp, (r, i) -> {
            Movie m = new Movie();
            m.setMovie_id(r.getInt("movie_id"));
            m.setTitle(r.getString("title"));
            m.setCategory(r.getString("category"));
            m.setDirector(r.getString("director"));
            m.setMusic(r.getString("music"));
            m.setDateOfCreation(r.getString("dateOfCreation"));
            m.setStudio(r.getString("studio"));
            m.setDescription(r.getString("description"));
            m.setRating(r.getString("rating"));
            m.setTrailer(r.getString("trailer"));
            m.setLanguage(r.getString("language"));
            m.setCountry(r.getString("country"));
            m.setPoster(r.getString("poster"));
            m.setCountryOfShooting(r.getString("countryOfShooting"));
            m.setDuration(r.getString("duration"));
            return m;
        });
    }

    public List<Movie> getMovieInfo(Integer movie_id) {
        //@formatter:off
        String sql = "SELECT movie_id        " +
                "     , category             " +
                "     , director             " +
                "     , music                " +
                "     , title                " +
                "     , dateOfCreation       " +
                "     , studio               " +
                "     , description          " +
                "     , rating               " +
                "     , poster               " +
                "     , trailer              " +
                "     , country              " +
                "     , imdbrating           " +
                "     , language             " +
                "     , countryOfShooting    " +
                "     , duration             " +
                "  FROM movies               " +
                " WHERE movie_id = :movie_id ";
        //@formatter:on
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("movie_id", movie_id);

        return namedTemplate.query(sql, sp, (r, i) -> {
            Movie m = new Movie();
            m.setMovie_id(r.getInt("movie_id"));
            m.setTitle(r.getString("title"));
            m.setCategory(r.getString("category"));
            m.setDirector(r.getString("director"));
            m.setMusic(r.getString("music"));
            m.setDateOfCreation(r.getString("dateOfCreation"));
            m.setStudio(r.getString("studio"));
            m.setDescription(r.getString("description"));
            m.setRating(r.getString("rating"));
            m.setTrailer(r.getString("trailer"));
            m.setLanguage(r.getString("language"));
            m.setCountry(r.getString("country"));
            m.setPoster(r.getString("poster"));
            m.setCountryOfShooting(r.getString("countryOfShooting"));
            m.setDuration(r.getString("duration"));
            return m;
        });
    }

    public List<Comment> getMovieComments(String title) {
        //@formatter:off
        String sql = "SELECT u.username, mc.comment, mc.date " +
                "  FROM movie_comments mc, movies m, user u   " +
                " WHERE 1 = 1                                 " +
                "   AND m.movie_id = mc.movie_id              " +
                "   AND mc.user_id = u.id                     " +
                "   AND m.title = :title                      ";
        //@formatter:on
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("title", title);
        return namedTemplate.query(sql, sp, (r, i) -> {
            Comment c = new Comment();
            c.setUsername(r.getString("username"));
            c.setComment(r.getString("comment"));
            c.setDate(r.getString("date"));
            return c;
        });
    }



    public List<String> whoWatchedIt(String title) {
        //@formatter:off
        String sql = "SELECT u.username                        " +
                     "  FROM user u, movies m, watched_movies w " +
                     " WHERE 1 = 1                              " +
                     "   AND u.id = w.user_id                   " +
                     "   AND m.movie_id = w.movie_id            " +
                     "   AND m.title = :title                   ";
        //@formatter:on
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("title", title);
        return namedTemplate.query(sql, sp, (r, i) -> r.getString("username"));
    }

    public List<Event> getAllEvents() {
        //@formatter:off
        String sql = "SELECT e.event_id, m.title, e.user_id, e.date, e.place " +
                "  FROM events e, movies m                               " +
                " WHERE 1 = 1                                           " +
                "   AND e.movie_id = m.movie_id                         " +
                " ORDER BY event_id                                     ";
        //@formatter:on
        return namedTemplate.query(sql, new MapSqlParameterSource(), (r, i) -> {
            Event e = new Event();
            e.setEvent_id(r.getInt("event_id"));
            e.setUser_id(r.getInt("user_id"));
            e.setName(r.getString("title"));
            e.setDate(r.getString("date"));
            e.setPlace(r.getString("place"));
            return e;
        });
    }

    public List<String> checkFreeUser(String email) {
        //@formatter:off
        String sql = "SELECT email          " +
                     " FROM user            " +
                     "WHERE email = :email; ";
        //@formatter:on
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("email", email);

        return namedTemplate.query(sql, sp, (r, i) -> r.getString("email"));
    }

    public List<String> getCategories() {
        //@formatter:off
        String sql = "SELECT category " +
                " FROM category       ";
        //@formatter:on

        return namedTemplate.query(sql, new MapSqlParameterSource(), (r, i) -> r.getString("category"));
    }

    public List<String> getCountries() {
        //@formatter:off
        String sql = "SELECT country " +
                " FROM country       ";
        //@formatter:on

        return namedTemplate.query(sql, new MapSqlParameterSource(), (r, i) -> r.getString("country"));
    }

    public List<String> getLanguages() {
        //@formatter:off
        String sql = "SELECT language " +
                " FROM language       ";
        //@formatter:on

        return namedTemplate.query(sql, new MapSqlParameterSource(), (r, i) -> r.getString("language"));
    }

    public List<Integer> getMaxMovieId() {
        final String sql = "SELECT MAX(movie_id) as id FROM movies";
        return namedTemplate.query(sql, new MapSqlParameterSource(), (r, i) -> r.getInt("id"));
    }

    public List<Movie> getMovieByTitleAndYear(String title, String date) {
        final String sql = "SELECT * FROM movies WHERE Title = :title AND dateOfCreation = :dateOfCreation";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", title)
                .addValue("dateOfCreation", date);
        return namedTemplate.query(sql, params, (r, i) -> {
            Movie existing = new Movie();
            existing.setTitle(r.getString("title"));
            existing.setDateOfCreation(r.getString("dateOfCreation"));
            return existing;
        });

    }

    public List<Integer> getMostRecentUserId() {
        final String sql = "SELECT MAX(id) as id FROM user";
        return namedTemplate.query(sql, new MapSqlParameterSource(), (r, i) -> r.getInt("id"));
    }

    public List<User> getUserById(int id) {
        final String sql = "SELECT * FROM user WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return namedTemplate.query(sql, params, (r, i) -> {
            User user = new User();
            user.setId(r.getInt("id"));
            return user;
        });
    }

    public List<Integer> getWishlistUserIdsByMovieId(Integer movieId) {
        final String sql = "SELECT user_id FROM wished_movies WHERE movie_id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", movieId);
        return namedTemplate.query(sql, params, (r, i) -> r.getInt("user_id"));
    }

    public List<Integer> isMovieInUserWishlist(Integer movieId, Integer userId) {
        final String sql = "SELECT movie_id FROM wished_movies WHERE movie_id = :id AND user_id = :user_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", movieId)
                .addValue("user_id", userId);
        return namedTemplate.query(sql, params, (r, i) -> r.getInt("movie_id"));
    }

    public List<Integer> isMovieInUserWatchedlist(Integer movieId, Integer userId) {
        final String sql = "SELECT movie_id FROM watched_movies WHERE movie_id = :id AND user_id = :user_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", movieId)
                .addValue("user_id", userId);
        return namedTemplate.query(sql, params, (r, i) -> r.getInt("movie_id"));
    }

    public List<String> checkFreeUserByUsername(String username) {
        //@formatter:off
        String sql = "SELECT username          " +
                " FROM user            " +
                "WHERE username = :username; ";
        //@formatter:on
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("username", username);

        return namedTemplate.query(sql, sp, (r, i) -> r.getString("username"));
    }

    public List<User> getUserInfo(String name) {
        final String sql = "SELECT username, user_id, userRating FROM user WHERE username = :name";
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("name", name);

        return namedTemplate.query(sql, sp, (r, i) -> {
            User user = new User();
            user.setName(r.getString("username"));
            user.setId(r.getInt("id"));
            user.setUserRating(r.getInt("userRating"));
            return user;
        });
    }

    public List<Integer> getMovieRatings(Integer id) {
        final String sql = "SELECT rating FROM movie_ratings WHERE movie_id = :id";
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("id", id);
        return namedTemplate.query(sql, sp, (r, i) -> r.getInt("rating"));
    }

    public List<Integer> getUserRatings(Integer id) {
        final String sql = "SELECT rating FROM user_ratings WHERE user_id = :id";
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("id", id);
        return namedTemplate.query(sql, sp, (r, i) -> r.getInt("rating"));
    }

    public List<Movie> getMoviesAddedByUser(Integer user_id) {
        final String sql = "SELECT movie_id          " +
                "     , category          " +
                "     , director          " +
                "     , music             " +
                "     , title             " +
                "     , dateOfCreation    " +
                "     , studio            " +
                "     , description       " +
                "     , rating            " +
                "     , poster            " +
                "     , trailer           " +
                "     , country           " +
                "     , imdbrating        " +
                "     , language          " +
                "     , countryOfShooting " +
                "     , duration          " +
                "  FROM movies           " +
                "WHERE added_by = :id " +
                "ORDER BY dateOfCreation DESC";

        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("id", user_id);

        return namedTemplate.query(sql, sp, (r, i) -> {
            Movie m = new Movie();
            m.setMovie_id(r.getInt("movie_id"));
            m.setCategory(r.getString("category"));
            m.setDirector(r.getString("director"));
            m.setMusic(r.getString("music"));
            m.setTitle(r.getString("title"));
            m.setDateOfCreation(r.getString("dateOfCreation"));
            m.setStudio(r.getString("studio"));
            m.setDescription(r.getString("description"));
            m.setRating(r.getString("rating"));
            m.setPoster(r.getString("poster"));
            m.setTrailer(r.getString("trailer"));
            m.setCountry(r.getString("country"));
            m.setImdbRating(r.getInt("imdbrating"));
            m.setLanguage(r.getString("language"));
            m.setLanguage(r.getString("countryOfShooting"));
            m.setLanguage(r.getString("duration"));
            return m;
        });
    }

    public List<ConfirmationToken> getConfirmationToken(String token) {
        //@formatter:off
        String sql = "SELECT user_email " +
                " FROM confirmation_token " +
                "WHERE confirmation_token = :confirmation_token; ";
        //@formatter:on

        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("confirmation_token", token);
        return namedTemplate.query(sql, sp, (r, i) ->
        {
            ConfirmationToken ct = new ConfirmationToken();
            ct.setEmail(r.getString("user_email"));
            return ct;
        });
    }

    public List<Integer> checkActiveUser(Integer id) {
        //@formatter:off
        String sql = "SELECT active " +
                " FROM user " +
                "WHERE id = :id; ";
        //@formatter:on

        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("id", id);
        return namedTemplate.query(sql, sp, (r, i) -> r.getInt("active"));
    }

    public List<Event> getEventsPerUser(Integer userId) {
        final String sql = "SELECT e.event_id, e.movie_id, e.user_id, e.date, e.place, m.title" +
                " FROM events e, movies m " +
                "WHERE е.user_id = :id" +
                " AND e.movie_id = m.movie_id" +
                " ORDER BY event_id ";
        final SqlParameterSource sp = new MapSqlParameterSource()
                .addValue("id", userId);
        return namedTemplate.query(sql, sp, (r, i) -> {
            Event event = new Event();
            event.setDate(r.getString("date"));
            event.setEvent_id(r.getInt("event_id"));
            event.setMovie_id(r.getInt("movie_id"));
            event.setName(r.getString("title"));
            event.setUser_id(r.getInt("user_id"));
            event.setPlace(r.getString("place"));
            return event;
        });
    }

    public List<Festival> getFestivals() {
        final String sql = "SELECT *" +
                " FROM festivals" +
                "ORDER BY id";
        return namedTemplate.query(sql, new MapSqlParameterSource(), (r, i) -> {
            Festival f = new Festival();
            f.setName("name");
            f.setDescription("description");
            f.setEndDate("endDate");
            f.setStartDate("startDate");
            f.setLink("link");
            f.setUrl("url");
            f.setPlace("place");
            return f;
        });
    }
}
