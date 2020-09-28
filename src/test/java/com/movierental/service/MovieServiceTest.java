package com.movierental.service;

import com.movierental.exception.BusinessException;
import com.movierental.model.Movie;
import com.movierental.model.User;
import com.movierental.repository.MovieRepository;
import com.movierental.repository.UserMovieRepository;
import com.movierental.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MovieServiceTest {

    //private MovieService movieService = new MovieService();

   /* @MockBean
    private MovieRepository movieRepository;
    @Mock
    private UserMovieRepository userMovieRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() { movieService = new MovieService(movieRepository, userMovieRepository, userRepository);}
*/
    @Test
    void getAllAvailableMovies() {
        Movie a = new Movie(1L, "teste1", "ElDiretor", 1, 1);
        Movie b = new Movie(2L, "teste2", "ElDiretor", 1, 0);
        Movie c = new Movie(3L, "teste3", "ElDiretor2", 1, 1);
        List<Movie> movies = new ArrayList<>();
        movies.add(a); movies.add(b); movies.add(c);
        List<Movie> available = new ArrayList<>();
        available.add(a); available.add(c);

        Iterator<Movie> itr = movies.iterator();
        while(itr.hasNext()){
            int temp = itr.next().getAvailable();
            if(temp == 0){
                itr.remove();
            }
        }

        assertEquals(movies, available);


    }


  /*  @Test
    void rentMovieMovieIsNullBusinessException() {
        Movie m = null;
        User u = new User();
        try{
            movieService.rentMovie(m, u);
            fail("Deveria ter lançado exceção!");
        }catch (BusinessException e){
            assertEquals("Object Movie is null", e.getMessage());
        }

    }

    @Test
    void rentMovieUserIsNullBusinessException() {
        Movie m = new Movie();
        User u = null;
        try{
            movieService.rentMovie(m, u);
            fail("Deveria ter lançado exceção!");
        }catch (BusinessException e){
            assertEquals("Object User is null, parameters username from request must have some issue", e.getMessage());
        }

    }*/

    @Test
    void giveMovieBack() {
    }
}