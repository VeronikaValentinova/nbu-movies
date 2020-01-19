package com.movieproject.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieproject.Entity.Movie;


public interface MovieRepository extends JpaRepository<Movie, Long> {

}
