package com.movierental.model;

import javax.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_title")
    private String title;
    @Column(name = "movie_director")
    private String director;
    @Column(name = "movie_amount")
    private int amount;
    @Column(name = "movie_amount_available")
    private int available;

    public Movie(){}

    public Movie(Long id, String title, String director, int amount, int available) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.amount = amount;
        this.available = available;
    }

    public Movie(String title, String director) {
        this.title = title;
        this.director = director;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", amount=" + amount +
                ", available=" + available +
                '}';
    }
}
