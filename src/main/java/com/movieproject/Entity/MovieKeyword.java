package com.movieproject.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "movie_keywords")
public class MovieKeyword {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int movieid;
	
	@Column(name = "keyword")
	private String keyword;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="filmid", nullable = false)
	private Movie movie;
	
	public String getComment() {
		return keyword;
	}
	
	public int getMovieId() {
		return movieid;
	}
	
	
}
