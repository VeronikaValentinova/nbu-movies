package com.movieproject;

import com.movieproject.bean.*;
import com.movieproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
public class Controller {

	@Autowired
	UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam("email") @Valid String email, @RequestParam("password") String pass, @RequestParam("token") String confirmationToken) {
		userService.confirmUserAccount(confirmationToken);
		return userService.login(email, pass);
	}

//	@RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
//	public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken)
//	{
//		return userService.confirmUserAccount(confirmationToken);
//	}

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

	@GetMapping("/home/movies/movie/check") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> checkExistingMovie(@RequestHeader("Authorization") String token, @RequestBody Movie movie) {
		return userService.checkExistingMovie(movie);
	}

	@GetMapping("/home/movies/movie/wishlist/count")
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<Integer> wishlistsContainingMovieNum(@RequestParam Integer movie_id) {
		return userService.wishlistsContainingMovieNum(movie_id);
	}

	@GetMapping("/home/movies/movie/wished") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<Integer> isMovieInUserWishlist(@RequestHeader("Authorization") String token, @RequestParam Integer movie_id) {
		return userService.isMovieInUserWishlist(movie_id, token);
	}

	@GetMapping("/home/movies/movie/watched") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<Integer> isMovieInUserWatchedlist(@RequestHeader("Authorization") String token, @RequestParam Integer movie_id) {
		return userService.isMovieInUserWatchedlist(movie_id, token);
	}

	@GetMapping("/home/user") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public User getUserInfo(@RequestParam String name) {
		return userService.getUserInfo(name);
	}

	@GetMapping("/home/user/movies") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<Movie> getMoviesAddedByUser(@RequestHeader("Authorization") String token) {
		return userService.getMoviesAddedByUser(token);
	}

	@GetMapping("/home/user/events") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public List<Event> getEventsPerUser(@RequestHeader("Authorization") String token) {
		return userService.getEventsPerUser(token);
	}

	/*****************************************************************************
	 *
	 *                          UPDATE / INSERT in DB
	 *
	 ****************************************************************************/


	@PostMapping("/home/movies/wishList/add") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> addMovieToWishList(@RequestHeader("Authorization") String token, @RequestBody() Integer movie_id) {
		return userService.addMovieToWishList(token, movie_id);
	}

	@PostMapping("/home/movies/watchedList/add") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> addMovieToWatchedList(@RequestHeader("Authorization") String token, @RequestBody() Integer movie_id) {
		return userService.addMovieToWatchedList(token, movie_id);
	}

	@PostMapping("/home/movies/wishList/remove") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> removeMovieFromWishList(@RequestHeader("Authorization") String token, @RequestBody() Integer movie_id) {
		return userService.removeMovieFromWishList(token, movie_id);
	}

	@PostMapping("/home/movies/watchedList/remove") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> removeMovieToWatchedList(@RequestHeader("Authorization") String token, @RequestBody() Integer movie_id) {
		return userService.removeMovieFromWatchedList(token, movie_id);
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
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
		return userService.addMovie(movie);
	}

	@PostMapping("/home/movies/movie/add/poster") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> addMoviePoster(@RequestPart String title, @RequestPart MultipartFile file) {
		return userService.addMoviePoster(title, file);
	}

	@PostMapping("/home/users/user/changepass") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @RequestParam String password) {
		return userService.changePassword(token, password);
	}

	@PostMapping("home/users/archive") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> archiveUser(@RequestHeader("Authorization") String token) {
		return userService.archiveOrRestoreUser(token, 1);
	}

	@PostMapping("/home/categories/archive") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> archiveCategory(@RequestParam String category) {
		return userService.archiveOrRestoreCategory(category, 1);
	}

	@PostMapping("/home/movie/archive") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> archiveMovie(@RequestParam Integer movie_id) {
		return userService.archiveOrRestoreMovie(movie_id, 1);
	}

	@PostMapping("home/users/restore") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> restoreUser(@RequestHeader("Authorization") String token) {
		return userService.archiveOrRestoreUser(token, 0);
	}

	@PostMapping("/home/categories/restore") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> restoreCategory(@RequestParam String category) {
		return userService.archiveOrRestoreCategory(category, 0);
	}

	@PostMapping("/home/movie/restore") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> restoreMovie(@RequestParam Integer movie_id) {
		return userService.archiveOrRestoreMovie(movie_id, 0);
	}

	@PostMapping("/home/users/user/rate") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> rateUser(@RequestParam Integer user_id, @RequestParam Integer rating) {
		return userService.rateUser(user_id, rating);
	}

	@PostMapping("/home/movies/movie/rate") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> rateMovie(@RequestParam Integer movie_id, @RequestParam Integer rating) {
		return userService.rateMovie(movie_id, rating);
	}

	@PostMapping("/home/events/add") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> addEvent(@RequestHeader("Authorization") String token, @RequestParam Event event) {
		return userService.addEvent(event, token);
	}

	@PostMapping("/home/movies/movie/del") // NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> deleteMovie(@RequestParam Integer movie_id) {
		return userService.deleteMovie(movie_id);
	}

	@PostMapping("/home/movies/movie/actor/del") //NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> deleteActor(@RequestParam Integer movie_id, @RequestParam Integer id) {
		return userService.deleteActor(movie_id, id);
	}

	@PostMapping("/home/movies/movie/actor/add") //NEW
	@PreAuthorize(("@securityService.hasAccess(#token)"))
	public ResponseEntity<String> addActor(@RequestBody Actor actor) {
		return userService.addActor(actor);
	}

}
