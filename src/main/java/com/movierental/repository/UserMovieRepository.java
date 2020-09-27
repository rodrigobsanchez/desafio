package com.movierental.repository;

import com.movierental.model.UserMovie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserMovieRepository extends CrudRepository<UserMovie, Long> {
    List<UserMovie> findByUserIdAndTitle(Long userId, String title);
    List<UserMovie> findByUsername(String username);
}
