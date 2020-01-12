package com.movieproject.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.movieproject.Entity.Movie;
import com.movieproject.Repositories.MovieRepository;

@Service
@Primary
public class MovieServiceImpl implements MovieService {
	@Autowired
	MovieRepository movieRepository;
	@Override
	public List<Movie> findAll()
	{
		return movieRepository.findAll();
	}
	@Override
    public Movie findById(Long id) 
	{
		Optional<Movie> movieRepository = this.movieRepository.findById(id);
		if (movieRepository.isPresent()) {
		    return movieRepository.get();
		} else {
		    return null;
		}
	}
	@Override
    public Movie create(Movie movie) 
	{
		return movieRepository.save(movie);
	}
	@Override
    public Movie edit(Movie movie) 
	{
		return movieRepository.save(movie);
	}
	@Override
    public void deleteById(Long id) 
	{
		movieRepository.deleteById(id);
	}
}
