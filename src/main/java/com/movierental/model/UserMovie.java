package com.movierental.model;

import javax.persistence.*;

@Entity
@Table(name = "user_movies")
public class UserMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private long userId;
    @Column(name = "username")
    private String username;
    @Column(name = "movie_title")
    private String title;

    public UserMovie(){}

    public UserMovie(Long id, long userId, String username, String title) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.title = title;
    }

    public UserMovie(long userId, String username, String title) {
        this.userId = userId;
        this.username = username;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
