package com.movieproject.Controller;

import com.movieproject.Entity.Movie;
import com.movieproject.Service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

import javax.validation.Valid;

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

    @RequestMapping(value = "/addmovie", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView insertMovie() {
        ModelAndView model = new ModelAndView();
        Movie createmovie = new Movie();
        model.addObject("createmovie", createmovie);
        model.setViewName("addmovie"); //ADDMOVIE HTML PAGE WITH FORM FOR THE CREATEMOVIE OBJECT (THYMELEAF)
        
        return model;
    }
    
    @RequestMapping(value = "/addmovie", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView insertMovie(@Valid Movie movie, BindingResult bindingResult) {
    	ModelAndView model = new ModelAndView();
    	
    	movieService.insertMovie(movie);
        model.addObject("createmovie", new Movie());
        model.setViewName("movies"); //ALL MOVIES PAGE
        
        return model;
    }

}
