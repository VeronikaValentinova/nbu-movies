package com.movieproject.services;

import com.movieproject.bean.*;
import com.movieproject.dao.PostDao;
import com.movieproject.dao.GetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

	@Autowired
	SecurityService securityService;

	private final GetDao getDao;
	private final PostDao postDao;

	public UserService(GetDao getDao, PostDao postDao) {
		this.getDao = getDao;
		this.postDao = postDao;
	}

	public ResponseEntity<?> login(String email, String pass) {
		List<User> usList;
		usList = getDao.getUserRole(email, pass);
		if (!usList.isEmpty()) {
			User user = usList.get(0);

			user.setToken("Bearer " + securityService.generateToken());
			System.out.println(user.getToken());
			SecurityService.users.add(user);

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Access-Control-Allow-Origin", "*");
			responseHeaders.set("Access-Control-Expose-Headers", "Authorization, Admin, Name, User_id");
			responseHeaders.set("Authorization", user.getToken());
			responseHeaders.set("Admin", user.getRole());
			responseHeaders.set("Name", user.getName());
			responseHeaders.set("User_id", user.getId() + "");

			return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("550 - No such user!");
		}
	}

	public List<Movie> getAllMovies() {
		List<Movie> movieList = getDao.getAllMovies();
		List<String> actors;
		List<String> mainActors;
		List<String> awards;
		List<Comment> comments;

		for (Movie movie: movieList) {
			actors = getDao.getMovieActors(movie.getTitle());
			movie.setActors( actors);

			mainActors = getDao.getMovieMainActors(movie.getTitle());
			movie.setMainActors(mainActors);

			awards = getDao.getMovieAwards(movie.getTitle());
			movie.setAwards(awards);

			comments = getDao.getMovieComments(movie.getTitle());
			movie.setComments(comments);
		}
		return movieList;
	}

	public List<Movie> getUserWishList(String token) {
		return getDao.getUserWishList(securityService.getUserId(token));
	}

	public List<Movie> getUserWatchedList(String token) {
		return getDao.getUserWatchedList(securityService.getUserId(token)); }

	public Movie getMovieInfo(Integer movie_id) {
		List<Movie> l = getDao.getMovieInfo(movie_id);
		if (!l.isEmpty()) {
			Movie m = l.get(0);
			List<String> actors = getDao.getMovieActors(m.getTitle());
			m.setActors(actors);
			List<String> mainActors = getDao.getMovieMainActors(m.getTitle());
			m.setMainActors(mainActors);
			List<String> awards = getDao.getMovieAwards(m.getTitle());
			m.setAwards(awards);
			List<Comment> comments = getDao.getMovieComments(m.getTitle());
			m.setComments(comments);
			return m;
		} else return null;
	}

	public List<String> whoWatchedIt(String title) { return getDao.whoWatchedIt(title); }

	public List<Event> getAllEvents() {
		List<Event> l;
		l = getDao.getAllEvents();
		return l;
	}

	public ResponseEntity<String> checkFreeUser(String email) {
		List<String> l = getDao.checkFreeUser(email);
		if (l.isEmpty()) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().body("User already exists!");
	}

	public List<String> getCategories() {
		return getDao.getCategories();
	}

	public List<String> getCountries() {
		return getDao.getCountries();
	}

	public List<String> getLanguages() {
		return getDao.getLanguages();
	}

//	public List<Event> getUserEvents() {
//
//	}





	public ResponseEntity<String> addMovieToWishList(String token, Integer movie_id) {
		List<Movie> movies = getUserWishList(token);
		for (Movie m : movies) {
			if (m.getMovie_id().equals(movie_id))
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Movie already in wishlist");
		}
		postDao.addMovieToWishList(securityService.getUserId(token), movie_id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	public void saveMovieComment(String token, Comment comment) throws ParseException {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date dt = sdf.parse(comment.getDate());
		String currentTime = sdf.format(dt);
		postDao.saveMovieComment(securityService.getUserId(token), comment.getTitle(), comment.getComment(), currentTime);
	}

	public ResponseEntity<String> saveUser(String email, String pass, String username) {
		List<String> l = getDao.checkFreeUser(email);
		if (l.isEmpty()) {
			postDao.saveUser(email, pass, username);
			List<Integer> latestUserId = getDao.getMostRecentUserId();
			int id = 0;
			if (!latestUserId.isEmpty())
				id = latestUserId.get(0);
			List<User> newUser = getDao.getUserById(id);
			if (newUser.isEmpty())
				return ResponseEntity.status(HttpStatus.CONFLICT).body("User not signed up!");
			if (postDao.setUserRole(newUser.get(0).getId()) == 1)
				return ResponseEntity.status(HttpStatus.OK).build();
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User not signed up!");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists!");
	}

	public int deleteEvent(Integer event_id) {
		return postDao.deleteEvent(event_id);
	}

	public ResponseEntity<String> addMovie(Movie movie) {
		List<Integer> movieIds = getDao.getMaxMovieId();
		int movieId = 0;
		if (!movieIds.isEmpty())
			movieId = movieIds.get(0) + 1;

		List<Movie> mList = getDao.getMovieByTitleAndYear(movie.getTitle(), movie.getDateOfCreation());
		if (mList.isEmpty()) {
			if (postDao.addMovie(movie) == 1)
				return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(movieId));
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Adding movie failed");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Movie already exists!");
	}

	public ResponseEntity<String> checkActiveToken(String token) {
		if (token == null)
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		boolean tokenActive = securityService.getUserId(token) != -1;
		if (tokenActive)
			return ResponseEntity.status(HttpStatus.OK).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> checkExistingMovie(Movie movie) {
		List<Movie> mList = getDao.getMovieByTitleAndYear(movie.getTitle(), movie.getDateOfCreation());
		if (mList.isEmpty())
			return ResponseEntity.status(HttpStatus.OK).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> addMovieToWatchedList(String token, Integer movie_id) {
		List<Movie> watchedMovies = getUserWatchedList(token);
		for (Movie m : watchedMovies) {
			if (m.getMovie_id().equals(movie_id))
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Movie already in watchlist");
		}
		postDao.addMovieToWatchedList(securityService.getUserId(token), movie_id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	public ResponseEntity<String> addMoviePoster(String title, MultipartFile file) {
		final Path loc  = Paths.get("/static/images/");
		try {
			try {
				Files.copy(file.getInputStream(), loc.resolve(title));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Image upload failed");
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Image upload failed");
		}
	}
}