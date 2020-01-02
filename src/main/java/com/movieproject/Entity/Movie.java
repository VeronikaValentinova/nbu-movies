package com.movieproject.Entity;

import java.sql.Date;
import java.util.List;

public class Movie {

    private int filmid;
    private String title;
    private String category;
    private List<String> actors;
    private String director;
    private String music;
    private Date date;
    private String studio;
    private String Description;
    private int rating;
    private String trailer;
    private int length;
    private String language;
    private String countryfilmed;
    private int imdbrating;
    private List<String> keywords;
    private List<String> comments;

    public Movie() {}

    public Movie(int filmid, String title, String category, List<String> actors, String director, String music, Date date,
    		String studio, String Description, int rating, String trailer, int length, String language, String countryfilmed,
    		int imdbrating,List<String> keywords) 
    {
    	this.filmid = filmid;
        this.title = title;
        this.category = category;
        this.actors = actors;
        this.director = director;
        this.music = music;
        this.date = date;
        this.studio = studio;
        this.Description = Description;
        this.rating = rating;
        this.trailer = trailer;
        this.length = length;
        this.language = language;
        this.countryfilmed = countryfilmed;
        this.imdbrating = imdbrating;
        this.keywords = keywords;        
    }

    public int getFilmid() {
        return filmid;
    }

    public void setFilmid(int filmid) {
    	this.filmid = filmid;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public List<String> getActors() {
        return actors;
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
    
    public List<String> getKeywords() {
        return keywords;
    }
    
    public List<String> getComments() {
        return comments;
    }
}
