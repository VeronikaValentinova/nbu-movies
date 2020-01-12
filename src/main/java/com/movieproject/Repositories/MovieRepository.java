package com.movieproject.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movieproject.Entity.Movie;

@Repository("movieRepository")
public interface MovieRepository extends JpaRepository<Movie, Long> {
	
}
