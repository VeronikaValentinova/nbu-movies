package com.movieproject.services;

import com.movieproject.bean.*;
import com.movieproject.dao.PostDao;
import com.movieproject.dao.GetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

@Service
public class UserService {

	@Autowired
	SecurityService securityService;

	@Autowired
	EmailSenderService emailSenderService;

	private final GetDao getDao;
	private final PostDao postDao;

	public UserService(GetDao getDao, PostDao postDao) {
		this.getDao = getDao;
		this.postDao = postDao;
	}

	public ResponseEntity<?> login(String emailOrUsername, String pass) {
		List<User> usList;
		usList = getDao.getUserRole(emailOrUsername, pass);

		if (usList == null || usList.isEmpty())
			usList = getDao.getUserRoleByUsername(emailOrUsername, pass);

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
		List<Actor> actors;
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
			List<Actor> actors = getDao.getMovieActors(m.getTitle());
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
		List<String> l1 =  getDao.checkFreeUserByUsername(username);
		if (l1.isEmpty() && l.isEmpty()) {
			postDao.saveUser(email, pass, username);
			UserBody userBody;
			userBody = new UserBody(email,pass);
			ConfirmationToken ct = new ConfirmationToken(userBody);
			postDao.saveConfirmationToken(ct);

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(userBody.getEmail());
			mailMessage.setSubject("Complete Registration!");
			mailMessage.setFrom("movieprojectnbu@gmail.com");
			mailMessage.setText("To confirm your account, please click here : "
					+"http://localhost:4200/confirm-account?token="+ct.getConfirmationToken());

			emailSenderService.sendEmail(mailMessage);
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
			if (postDao.addMovie(movie) == 1) {
				addAwards(movie);
				addActors(movie);
				//addComments(movie); not sure if this is needed, there shouldn't be any comments when adding a movie
				addKeywords(movie);
				addMainActors(movie);
				return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(movieId));
			}
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Adding movie failed");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Movie already exists!");
	}

	private void addMainActors(Movie movie) {
		List<String> mainActors = movie.getMainActors();
		if (mainActors.isEmpty())
			return;
		for (String s : mainActors) {
			postDao.addMovieMainActor(s, movie.getMovie_id());
		}
	}

	private void addKeywords(Movie movie) {
		List<String> keywords = movie.getKeywords();
		if (keywords.isEmpty())
			return;
		for (String s : keywords) {
			postDao.addMovieKeyword(s, movie.getMovie_id());
		}
	}

	//MAYBE not needed

//	private void addComments(Movie movie) {
//		List<Comment> comments = movie.getComments();
//		if (comments.isEmpty())
//			return;
//		for (Comment c : comments) {
//			postDao.addMovieComment(c, movie.getMovie_id());
//		}
//	}

	private void addActors(Movie movie) {
		List<Actor> actors = movie.getActors();
		if (actors.isEmpty())
			return;
		for (Actor actor : actors) {
			postDao.addMovieActor(actor, movie.getMovie_id());
		}
	}

	private void addAwards(Movie movie) {
		List<String> awards = movie.getAwards();
		if (awards.isEmpty())
			return;
		for (String award : awards) {
			postDao.addMovieAward(award, movie.getMovie_id());
		}
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

	public ResponseEntity<Integer> wishlistsContainingMovieNum(Integer movieId) {
		List<Integer> wishlistUserIds = getDao.getWishlistUserIdsByMovieId(movieId);
		return ResponseEntity.status(HttpStatus.OK).body(wishlistUserIds.size());
	}


	public ResponseEntity<String> removeMovieFromWatchedList(String token, Integer movie_id) {
		if (postDao.removeMovieFromWatchedList(movie_id) > 0)
			return ResponseEntity.status(HttpStatus.OK).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> removeMovieFromWishList(String token, Integer movie_id) {
		if (postDao.removeMovieFromWishList(movie_id) > 0)
			return ResponseEntity.status(HttpStatus.OK).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<Integer> isMovieInUserWishlist(Integer movieId, String token) {
		Integer userId = securityService.getUserId(token);
		if (!getDao.isMovieInUserWishlist(movieId, userId).isEmpty())
			return ResponseEntity.status(HttpStatus.OK).body(1);
		return ResponseEntity.status(HttpStatus.OK).body(0);
	}

	public ResponseEntity<Integer> isMovieInUserWatchedlist(Integer movieId, String token) {
		Integer userId = securityService.getUserId(token);
		if (!getDao.isMovieInUserWatchedlist(movieId, userId).isEmpty())
			return ResponseEntity.status(HttpStatus.OK).body(1);
		return ResponseEntity.status(HttpStatus.OK).body(0);
	}

	public ResponseEntity<String> changePassword(String token, String password) {
		Integer userId = securityService.getUserId(token);
		if (userId != null) {
			if (postDao.changePassword(userId, password) == 1);
				return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> archiveOrRestoreUser(String token, Integer param) {
		Integer userId = securityService.getUserId(token);
		if (userId != null) {
			if (postDao.archiveOrRestoreUser(userId, param) == 1);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> archiveOrRestoreCategory(String category, Integer param) {
		if (postDao.archiveOrRestoreCategory(category, param) == 1)
			return ResponseEntity.status(HttpStatus.OK).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> archiveOrRestoreMovie(Integer movie_id, Integer param) {
		if (postDao.archiveOrRestoreMovie(movie_id, param) == 1)
			return ResponseEntity.status(HttpStatus.OK).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public User getUserInfo(String name) {
		List<User> getUserInfo = getDao.getUserInfo(name);
		if (!getUserInfo.isEmpty())
			return getUserInfo.get(0);
		return null;
	}

	public ResponseEntity<String> rateMovie(Integer movie_id, Integer rating) {
		if (postDao.rateMovie(movie_id, rating) == 1) {
			List<Integer> currentMovieRatings = getDao.getMovieRatings(movie_id);
			Integer avg = 0;
			if (!currentMovieRatings.isEmpty()) {
				Integer sum = 0;
				for (Integer i : currentMovieRatings) {
					sum += i;
				}
				avg = sum/currentMovieRatings.size();
				postDao.updateAverageMovieRating(avg, movie_id);
			}
			return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(avg));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> rateUser(Integer user_id, Integer rating) {
		if (postDao.rateUser(user_id, rating) == 1) {
			List<Integer> currentUserRatings = getDao.getUserRatings(user_id);
			if (!currentUserRatings.isEmpty()) {
				Integer sum = 0;
				for (Integer i : currentUserRatings) {
					sum += i;
				}
				Integer avg = sum/currentUserRatings.size();
				postDao.updateAverageUserRating(avg, user_id);
			}
			return ResponseEntity.status(HttpStatus.OK).build();		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public List<Movie> getMoviesAddedByUser(String token) {
		Integer userId = securityService.getUserId(token);
		if (userId == null)
			return null;
		return getDao.getMoviesAddedByUser(userId);
	}

	public ResponseEntity<String> confirmUserAccount(String ct) {
		ConfirmationToken token = getDao.getConfirmationToken(ct).get(0);

		if(token.getEmail()!=null)
		{
			postDao.changeUserActive(token.getEmail());
			return ResponseEntity.status(HttpStatus.OK).body("User account activated !");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed at activation!");
	}

	public ResponseEntity<String> addEvent(Event event, String token) {
		Integer userId = securityService.getUserId(token);
		if (event == null || userId == null)
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		if (postDao.addEvent(event, userId) == 1)
			return ResponseEntity.status(HttpStatus.OK).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public List<Event> getEventsPerUser(String token) {
		Integer userId = securityService.getUserId(token);
		if (userId == null)
			return null;
		return getDao.getEventsPerUser(userId);
	}

	public ResponseEntity<String> deleteMovie(Integer movie_id) {
		if (postDao.deleteMovie(movie_id) == 1) {
			postDao.deleteComments(movie_id);
			postDao.deleteKeywords(movie_id);
			postDao.deleteActors(movie_id);
			postDao.deleteAwards(movie_id);
			postDao.deleteMainActors(movie_id);
			postDao.deleteRatings(movie_id);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> deleteActor(Integer movie_id, Integer actor_id) {
		if (postDao.deleteActor(movie_id, actor_id) == 1) {
			postDao.deleteMainActor(movie_id, actor_id); // if actor is a main actor at all
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	public ResponseEntity<String> addActor(Actor actor) {
		if (postDao.addActor(actor) == 1)
			return ResponseEntity.status(HttpStatus.OK).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
}