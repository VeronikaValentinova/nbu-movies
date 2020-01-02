package com.movieproject.Dao;

import com.movieproject.Entity.Movie;
import com.movieproject.Utility.JDBCUtility;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("exampleData") //needed to differentiate IMovieDao implementations
public class MovieDaoImpl implements IMovieDao {

	private static final String DELETE = "DELETE FROM movies WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM movies ORDER BY id";
	private static final String GET_BY_ID = "SELECT * FROM movies WHERE id=?";
	private static final String GET_BY_NAME = "SELECT * FROM movies WHERE title=?";
	private static final String INSERT = "INSERT INTO movies(title, category, actors, director, music, date, studio,"
			+ "Description, rating, trailer, length, language, countryfilmed, imdbrating, keywords )"
			+ " VALUES(?, ?, ?)";
	private static final String UPDATE = "UPDATE movies SET title=?, category=?, director=?, music=?"
			+ ", date=?, studio=?, Description=?, rating=?, trailer=?, length=?, language=?, countryfilmed=?"
			+ ", imdbrating=? WHERE id=?";


    //TODO
    @Override
    public List<Movie> getAllMovies(){
    	Connection conn = null;
		PreparedStatement stmt = null;
		List<Movie> list = new ArrayList<Movie>();
		
		try {
			conn = JDBCUtility.getConnection();
			stmt = conn.prepareStatement(FIND_ALL);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Movie movie = new Movie();
				movie.setFilmid(rs.getInt("id"));
				movie.setTitle(rs.getString("title"));
				movie.setCategory(rs.getString("category"));
				movie.setDirector(rs.getString("director"));
				movie.setMusic(rs.getString("music"));
				movie.setDate(rs.getDate("date"));
				movie.setStudio(rs.getString("studio"));
				movie.setDescription(rs.getString("Description"));
				movie.setRating(rs.getInt("rating"));
				movie.setTrailer(rs.getString("trailer"));
				movie.setLength(rs.getInt("length"));
				movie.setLanguage(rs.getString("language"));
				movie.setCountryfilmed(rs.getString("countryfilmed"));
				movie.setImdbrating(rs.getInt("imdbrating"));
				
				list.add(movie);
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			JDBCUtility.close(stmt);
			JDBCUtility.close(conn);
		}
		
		return list;
    }

    //TODO
    @Override
    public Movie getMovieById(int filmid) {
    	Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = JDBCUtility.getConnection();
			stmt = conn.prepareStatement(GET_BY_ID);
			stmt.setInt(1, filmid);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				Movie movie = new Movie();
				movie.setFilmid(rs.getInt("id"));
				movie.setTitle(rs.getString("title"));
				movie.setCategory(rs.getString("category"));
				movie.setDirector(rs.getString("director"));
				movie.setMusic(rs.getString("music"));
				movie.setDate(rs.getDate("date"));
				movie.setStudio(rs.getString("studio"));
				movie.setDescription(rs.getString("Description"));
				movie.setRating(rs.getInt("rating"));
				movie.setTrailer(rs.getString("trailer"));
				movie.setLength(rs.getInt("length"));
				movie.setLanguage(rs.getString("language"));
				movie.setCountryfilmed(rs.getString("countryfilmed"));
				movie.setImdbrating(rs.getInt("imdbrating"));
				
				return movie;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			JDBCUtility.close(stmt);
			JDBCUtility.close(conn);
		}
    }

    //TODO
    @Override
    public void removeMovieById(int filmid) {
    	Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = JDBCUtility.getConnection();
			stmt = conn.prepareStatement(DELETE);
			stmt.setInt(1, filmid);
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			JDBCUtility.close(stmt);
			JDBCUtility.close(conn);
		}
    }

    @Override
    public void updateMovie(Movie movie) {
    	
    	Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = JDBCUtility.getConnection();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setString(1, movie.getTitle());
			stmt.setString(2, movie.getCategory());
			stmt.setString(3, movie.getDirector());
			stmt.setString(4, movie.getMusic());
			stmt.setDate(5, movie.getDate());
			stmt.setString(6, movie.getStudio());
			stmt.setString(7, movie.getDescription());
			stmt.setInt(8, movie.getRating());
			stmt.setString(9, movie.getTitle());
			stmt.setString(10, movie.getTrailer());
			stmt.setInt(11, movie.getLength());
			stmt.setString(12, movie.getLanguage());
			stmt.setString(13, movie.getCountryfilmed());
			stmt.setInt(14, movie.getImdbrating());
			stmt.setInt(15, movie.getFilmid());
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			JDBCUtility.close(stmt);
			JDBCUtility.close(conn);
		}
    }

    @Override
    public void insertMovie(Movie movie) {
    	Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = JDBCUtility.getConnection();
			stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, movie.getTitle());
			stmt.setString(2, movie.getCategory());
			stmt.setString(3, movie.getDirector());
			stmt.setString(4, movie.getMusic());
			stmt.setDate(5, movie.getDate());
			stmt.setString(6, movie.getStudio());
			stmt.setString(7, movie.getDescription());
			stmt.setInt(8, movie.getRating());
			stmt.setString(9, movie.getTitle());
			stmt.setString(10, movie.getTrailer());
			stmt.setInt(11, movie.getLength());
			stmt.setString(12, movie.getLanguage());
			stmt.setString(13, movie.getCountryfilmed());
			stmt.setInt(14, movie.getImdbrating());
			stmt.setInt(15, movie.getFilmid());
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			JDBCUtility.close(stmt);
			JDBCUtility.close(conn);
		}
    }
}
