package com.movierental.service;

import com.movierental.model.Movie;
import com.movierental.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    DataSource dataSource;

    private final MovieRepository moviesRepository;

    public MovieService(MovieRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public List<Movie> getAll() {
        return (List<Movie>) moviesRepository.findAll();
    }
}
