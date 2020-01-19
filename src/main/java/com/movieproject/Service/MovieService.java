package com.movieproject.Service;

import com.movieproject.Entity.Movie;

import java.util.List;

public interface MovieService {

	List<Movie> findAllMovies();
    Movie findById(Long id);
    Movie create(Movie post);
    Movie edit(Movie post);
    void deleteById(Long id);
}
