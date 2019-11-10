package com.movieproject.Controller;

import com.movieproject.Entity.Movie;
import com.movieproject.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Movie> getAllMovies(){
        return movieService.getAllMovies();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Movie getMovieById(@PathVariable("id") int id) {
        return movieService.getMovieById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteMovieById(@PathVariable("id") int id) {
        movieService.removeMovieById(id);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateMovie(@RequestBody Movie movie) {
        movieService.updateMovie(movie);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertMovie(@RequestBody Movie movie) {
        movieService.insertMovie(movie);
    }

}
