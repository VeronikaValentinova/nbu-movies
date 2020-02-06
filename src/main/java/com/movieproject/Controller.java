package com.movieproject;

import com.movieproject.bean.Comment;
import com.movieproject.bean.Event;
import com.movieproject.bean.Movie;
import com.movieproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
public class Controller {

	@Autowired
	UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam("email") @Valid String email, @RequestParam("password") String pass) {
		return userService.login(email, pass);
	}

	@GetMapping("/home/movies")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<Movie> getAllMovies(@RequestHeader("Authorization") String token) {
		return userService.getAllMovies();
	}

	@GetMapping("/home/movies/wishList")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<Movie> getUserWishList(@RequestHeader("Authorization") String token) {
		return userService.getUserWishList(token);
	}

	@GetMapping("/home/movies/watchedList")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<Movie> getUserWatchedList(@RequestHeader("Authorization") String token) {
		return userService.getUserWatchedList(token);
	}

	@GetMapping("/home/movies/movie")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public Movie getMovieInfo(@RequestHeader("Authorization") String token, @RequestParam Integer movie_id) {
		return userService.getMovieInfo(movie_id);
	}


	@GetMapping("/home/movies/movie/watch")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<String> whoWatchedIt(@RequestHeader("Authorization") String token, @RequestParam String title) {
		return userService.whoWatchedIt(title);
	}

	@GetMapping("/home/events")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<Event> getUserEvents(@RequestHeader("Authorization") String token) {
		return userService.getAllEvents();
	}

	@GetMapping("/home/users/check")
	public ResponseEntity<String> checkFreeUser(@RequestParam String email) {
		System.out.println(email);
		return userService.checkFreeUser(email);
	}

	@GetMapping("/home/categories")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<String> getCategories(@RequestHeader("Authorization") String token) {
		return userService.getCategories();
	}

	@GetMapping("/home/countries")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<String> getCountries(@RequestHeader("Authorization") String token) {
		return userService.getCountries();
	}

	@GetMapping("/home/languages")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<String> getLanguages(@RequestHeader("Authorization") String token) {
		return userService.getLanguages();
	}

	@GetMapping("/home/users/user/token") // NEW
	public ResponseEntity<String> checkActiveToken(@RequestHeader("Authorization") String token) {
		return userService.checkActiveToken(token);
	}


	/*****************************************************************************
	 *
	 *                          UPDATE / INSERT in DB
	 *
	 ****************************************************************************/


	@PostMapping("/home/movies/wishList/add")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public void addMovieToWishList(@RequestHeader("Authorization") String token, @RequestBody() String title) {
		userService.addMovieToWishList(token, title);
	}

	@PostMapping("/home/movies/movie/comment/add")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public void saveMovieComment(@RequestHeader("Authorization") String token, @RequestBody() Comment comment) throws ParseException {
		userService.saveMovieComment(token, comment);
	}

	@PostMapping("/signup")
	public ResponseEntity<String> saveUser(@RequestParam String email, @RequestParam String password, @RequestParam String username) {
		return userService.saveUser(email, password, username);
	}

	@PostMapping("/home/events/del")
	public ResponseEntity<?> deleteEvent(@RequestParam Integer id) {
		if (userService.deleteEvent(id) == 1) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delete failed!");
	}
	@PostMapping("/home/movies/movie/add") // NEW
	public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
		return userService.addMovie(movie);
	}
}
