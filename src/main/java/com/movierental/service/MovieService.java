package com.movierental.service;

import com.movierental.model.Movie;
import com.movierental.model.User;
import com.movierental.model.UserMovie;
import com.movierental.repository.MovieRepository;
import com.movierental.repository.UserMovieRepository;
import com.movierental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    DataSource dataSource;

    private final MovieRepository moviesRepository;
    private final UserMovieRepository userMovieRepository;
    private final UserRepository userRepository;

    public MovieService(MovieRepository moviesRepository, UserMovieRepository userMovieRepository, UserRepository userRepository) {
        this.moviesRepository = moviesRepository;
        this.userMovieRepository = userMovieRepository;
        this.userRepository = userRepository;
    }


    public List<Movie> getAll() {
        return (List<Movie>) moviesRepository.findAll();
    }

    public List<Movie> getAllAvailableMovies(){
        List<Movie> movies = getAll();
        Iterator<Movie> itr = movies.iterator();
        while(itr.hasNext()){
            int temp = itr.next().getAvailable();
            if(temp == 0){
                itr.remove();
            }
        }
        return movies;
    }

    public List<String> getUserMovies(String userName){
        List<String> stringList = new ArrayList<>();
        List<UserMovie> list = userMovieRepository.findByUsername(userName);
        if(!list.isEmpty()){
            for(UserMovie temp : list){
                stringList.add(temp.getTitle());
            }
        }
        return stringList;
    }

    public Movie getMovieByTitle(String title)  {
        return moviesRepository.findByTitle(title);
    }

    public Movie rentMovie(Movie movie, User user){
        UserMovie userMovie = new UserMovie(user.getId(), user.getUsername(), movie.getTitle());
        insertUserMovie(userMovie);
        return moviesRepository.save(movie);
    }

    public Movie giveMovieBack(Movie movie, User user){
        List<UserMovie> temp = getUserMovie(user.getId(), movie.getTitle());
        deleteUserMovie(temp.get(0));
        movie.setAvailable(movie.getAvailable() + 1);
        return moviesRepository.save(movie);
    }

    private void insertUserMovie(UserMovie userMovie){
        userMovieRepository.save(userMovie);
    }
    private void deleteUserMovie(UserMovie userMovie){
        userMovieRepository.delete(userMovie);
    }

    private List<UserMovie> getUserMovie(Long userId, String title){
        return userMovieRepository.findByUserIdAndTitle(userId, title);
    }
}
