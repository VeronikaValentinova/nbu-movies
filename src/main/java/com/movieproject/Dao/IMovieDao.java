package com.movieproject.Dao;

import com.movieproject.Entity.Movie;

import java.util.Collection;

public interface IMovieDao {
    //TODO
    Collection<Movie> getAllMovies();

    //TODO
    Movie getMovieById(int id);

    //TODO
    void removeMovieById(int id);

    void updateMovie(Movie movie);

    void insertMovie(Movie movie);
}
