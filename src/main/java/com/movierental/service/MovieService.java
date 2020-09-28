package com.movierental.service;

import com.movierental.exception.BusinessException;
import com.movierental.model.Movie;
import com.movierental.model.User;
import com.movierental.model.UserMovie;
import com.movierental.repository.MovieRepository;
import com.movierental.repository.UserMovieRepository;
import com.movierental.repository.UserRepository;
import org.hibernate.query.criteria.internal.BasicPathUsageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    public List<String> getUserMovies(String userName) throws BusinessException {
        List<String> stringList = new ArrayList<>();

        if(Objects.isNull(userName)){
            throw new BusinessException("Username deve ser informado");
        }
        List<UserMovie> list = userMovieRepository.findByUsername(userName);
        if(!list.isEmpty()){
            for(UserMovie temp : list){
                stringList.add(temp.getTitle());
            }
        }
        return stringList;
    }

    public Movie getMovieByTitle(String title) throws BusinessException {
        if(Objects.isNull(title)){
            throw new BusinessException("Título deve ser informado");
        }
        return moviesRepository.findByTitle(title);
    }

    public Movie rentMovie(Movie movie, User user) throws BusinessException {
        if(Objects.isNull(movie)){
            throw new BusinessException("Object Movie is null");
        }
        if(Objects.isNull(user)){
            throw new BusinessException("Object User is null, parameters username from request must have some issue");
        }

        UserMovie userMovie = new UserMovie(user.getId(), user.getUsername(), movie.getTitle());
        insertUserMovie(userMovie);
        return moviesRepository.save(movie);
    }

    public Movie giveMovieBack(Movie movie, User user) throws BusinessException {
        if(Objects.isNull(user)){
            throw new BusinessException("Object User is null");
        }
        if(Objects.isNull(movie)){
            throw new BusinessException("Object Movie is null");
        }
        List<UserMovie> temp = getUserMovie(user.getId(), movie.getTitle());
        deleteUserMovie(temp.get(0));
        movie.setAvailable(movie.getAvailable() + 1);
        return moviesRepository.save(movie);
    }

    public Movie insert(Movie newMovie) throws BusinessException {
        Movie temp = getMovieByTitle(newMovie.getTitle());
        if(!Objects.isNull(temp)){
            temp.setAmount(temp.getAmount()+ 1);
            temp.setAvailable(temp.getAvailable() + 1);
            return moviesRepository.save(temp);
        } else {
            newMovie.setAmount(1);
            newMovie.setAvailable(1);
            return moviesRepository.save(newMovie);
        }
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
