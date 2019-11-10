package com.movieproject.Service;

import com.movieproject.Dao.IMovieDao;
import com.movieproject.Entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MovieService {

    @Autowired
    @Qualifier("exampleData") //needed to differentiate IMovieDao implementations
    private IMovieDao IMovieDao;

    public Collection<Movie> getAllMovies(){
        return IMovieDao.getAllMovies();
    }

    public Movie getMovieById(int id) {
        return IMovieDao.getMovieById(id);
    }

    public void removeMovieById(int id) {
        IMovieDao.removeMovieById(id);
    }

    public void updateMovie(Movie movie) {
        IMovieDao.updateMovie(movie);
    }

    public void insertMovie(Movie movie) {
        IMovieDao.insertMovie(movie);
    }
}
