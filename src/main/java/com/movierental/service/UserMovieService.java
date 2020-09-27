package com.movierental.service;

import com.movierental.model.UserMovie;
import com.movierental.repository.UserMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class UserMovieService {

    @Autowired
    DataSource dataSource;
    @Autowired
    UserMovieRepository userMovieRepository;

    public UserMovieService(){}

    public List<UserMovie> getAll() {
        return (List<UserMovie>) userMovieRepository.findAll();
    }

    public void insertUserMovie(UserMovie userMovie){
        userMovieRepository.save(userMovie);
    }

}
