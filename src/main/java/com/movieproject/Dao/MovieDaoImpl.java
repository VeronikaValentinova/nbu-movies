package com.movieproject.Dao;

import com.movieproject.Entity.Movie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Qualifier("exampleData") //needed to differentiate IMovieDao implementations
public class MovieDaoImpl implements IMovieDao {




    //TODO
    @Override
    public Collection<Movie> getAllMovies(){
        return null; // get all movies from db;
    }

    //TODO
    @Override
    public Movie getMovieById(int id) {
        return null; //get a single movie from db
    }

    //TODO
    @Override
    public void removeMovieById(int id) {
        // remove from db
    }

    @Override
    public void updateMovie(Movie movie) {
        Movie m = null; // TODO: get student by ID from db
        m.setName(movie.getName());
        // put into db by id
    }

    @Override
    public void insertMovie(Movie movie) {
        // TODO insert into db by id
    }
}
