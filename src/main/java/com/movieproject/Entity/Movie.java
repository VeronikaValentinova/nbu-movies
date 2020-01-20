package com.movieproject.Entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filmid;
	@Column(nullable = false, length = 300)
    private String Title;
	@Column(nullable = false)
    private String category;
	@Column(nullable = false)
    private String director;
	@Column(nullable = false)
    private String music;
	@Column(nullable = true)
    private Date date;
	@Column(nullable = false)
    private String studio;
	@Column(nullable = false)
    private String Description;
	@Column(nullable = true)
    private int rating;
	@Column(nullable = false)
    private String trailer;
	@Column(nullable = true)
    private int length;
	@Column(nullable = false)
    private String language;
	@Column(nullable = false)
    private String countryfilmed;
	@Column(nullable = false)
    private int imdbrating;
	
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="movie")
    private List<MovieComment> comments;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="movie")
	private List<MovieActor> actors;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="movie")
	private List<MovieKeyword> keywords;

    public int getFilmid() {
        return filmid;
    }

    public void setFilmid(int filmid) {
    	this.filmid = filmid;
    }
    
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
    
    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }
    
    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }
    
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
    
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getCountryfilmed() {
        return countryfilmed;
    }

    public void setCountryfilmed(String countryfilmed) {
        this.countryfilmed = countryfilmed;
    }
    
    public int getImdbrating() {
        return imdbrating;
    }

    public void setImdbrating(int imdbrating) {
        this.imdbrating = imdbrating;
    }
    
    
    public List<MovieComment> getComments() {
        return comments;
    }

    public List<MovieActor> getActors() {
        return actors;
    }

    public List<MovieKeyword> getKeywords() {
        return keywords;
    }
}
