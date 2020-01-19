package com.movieproject.Controller;

import com.movieproject.Entity.Movie;
import com.movieproject.Entity.User;
import com.movieproject.Service.MovieService;
import com.movieproject.Service.MovieServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
public class MovieController {
	@Autowired
	MovieServiceImpl movieservice;
	
	
	@GetMapping(value= {"/movies"}, produces = "application/json")
	 public List<Movie> movies() {
	  ModelAndView model = new ModelAndView();
	  List<Movie> movies = movieservice.findAllMovies();
	  model.addObject("movies", movies);
	  model.setViewName("home/movies");
	  
	  return movies;
	 }

}
