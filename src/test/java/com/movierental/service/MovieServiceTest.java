package com.movierental.service;

import com.movierental.exception.BusinessException;
import com.movierental.model.Movie;
import com.movierental.model.User;
import com.movierental.model.UserMovie;
import com.movierental.repository.MovieRepository;
import com.movierental.repository.UserMovieRepository;
import com.movierental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private  MovieService movieService;

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private UserMovieRepository userMovieRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAll(){
        List<Movie> list = Arrays.asList(
                new Movie(1L, "teste1", "ElDiretor", 1, 1),
                new Movie(2L, "teste2", "ElDiretor", 1, 0),
                new Movie(3L, "teste3", "ElDiretor2", 1, 1));
        Mockito.when(movieService.getAll()).thenReturn(list);
        List<Movie> temp = movieService.getAll();
        assertEquals(list, temp);
    }

    @Test
    void getAllAvailableMovies() {
        Movie a = new Movie(1L, "teste1", "ElDiretor", 1, 1);
        Movie b = new Movie(2L, "teste2", "ElDiretor", 1, 0);
        Movie c = new Movie(3L, "teste3", "ElDiretor2", 1, 1);
        List<Movie> movies = new ArrayList<>();
        movies.add(a); movies.add(b); movies.add(c);

        Mockito.when(movieService.getAll()).thenReturn(movies);

        List<Movie> available = movieService.getAllAvailableMovies();
        assertEquals(available, movies);
    }

    @Test
    void getUserMoviesUsernameNull() {
        String username = null;
        try{
            movieService.getUserMovies(username);
            fail("Deveria ter lançado exceção!");
        } catch (BusinessException e) {
            assertEquals("Username deve ser informado", e.getMessage());
        }
    }

    @Test
    void getUserMovies()  {
        List<UserMovie> list = Arrays.asList(
                new UserMovie(1L, 2L, "tester", "Nome 1"),
                new UserMovie(2L, 2L, "tester", "Nome 2"),
                new UserMovie(3L, 2L, "Josnildo", "Nome 3")
        );
        List<UserMovie> list2 = Arrays.asList(
                new UserMovie(1L, 2L, "tester", "Nome 1"),
                new UserMovie(2L, 2L, "tester", "Nome 2")
        );
        List<String> stringList = new ArrayList<>();

        for(UserMovie temp : list2){
            stringList.add(temp.getTitle());
        }

        try {
            Mockito.when(userMovieRepository.findByUsername("tester")).thenReturn(list2);
            List<String> answer = movieService.getUserMovies("tester");
            assertEquals(stringList, answer, "Deveria retornar lista de Strings com 2 elementos");

        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getMovieByTitleIsNull(){
        String title = null;
        try{
            movieService.getMovieByTitle(title);
            fail("Deveria ter lançado exceção!");
        }catch (BusinessException e){
            assertEquals("Título deve ser informado", e.getMessage());
        }
    }

    @Test
    void getMovieByTitle(){
        String title = "test2";
        List<Movie> list = Arrays.asList(
                new Movie(1L, "teste1", "ElDiretor", 1, 1),
                new Movie(2L, "teste2", "ElDiretor", 1, 0),
                new Movie(3L, "teste3", "ElDiretor2", 1, 1));

        List<Movie> listanswer = Arrays.asList(
                new Movie(2L, "teste2", "ElDiretor", 1, 0));
        Movie shouldBe = listanswer.get(0);

        Mockito.when(movieRepository.findByTitle(title)).thenReturn(shouldBe);


        try {
            Movie isIt = movieService.getMovieByTitle(title);
            assertEquals(shouldBe,isIt);
        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }

    @Test
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

    }

    @Test
    void rentMovie(){
        Movie movie = new Movie(38L, "Lord of the Rings", "Rowling", 2, 2);
        User user = new User("Testador", "tester@tester.com", "123456", "ROLE_USER", true);
        user.setId(45L);

        try {
            movieService.rentMovie(movie, user);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        Mockito.verify(movieRepository).save(movie);


    }

    @Test
    void giveMovieBackMovieIsNull() {
        Movie m = null;
        User u = new User();
        try{
            movieService.giveMovieBack(m, u);
            fail("Deveria ter lançado exceção!");
        }catch (BusinessException e){
            assertEquals("Object Movie is null", e.getMessage());
        }
    }

    @Test
    void giveMovieBackUserIsNull() {
        Movie m = new Movie();
        User u = null;
        try{
            movieService.giveMovieBack(m, u);
            fail("Deveria ter lançado exceção!");
        }catch (BusinessException e){
            assertEquals("Object User is null", e.getMessage());
        }
    }

    @Test
    void giveMovieBack(){
        Movie movie = new Movie(38L, "Lord of the Rings", "Rowling", 2, 2);
        User user = new User("Testador", "tester@tester.com", "123456", "ROLE_USER", true);
        user.setId(45L);
        UserMovie temp = new UserMovie();
        List<UserMovie> movierented = Arrays.asList(temp);


        Mockito.when(userMovieRepository.findByUserIdAndTitle(user.getId(), movie.getTitle())).thenReturn(movierented);

        try {
            movieService.giveMovieBack(movie, user);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        Mockito.verify(movieRepository).save(movie);
        Mockito.verify(userMovieRepository).delete(temp);
    }

    @Test
    void insertNewMovieNoMatchwithOtherMovie(){
        Movie newMovie = new Movie(38L, "Lord of the Rings", "Rowling", 1, 1);
        Movie resultByTitle = null;

        try {
            Mockito.when(movieService.getMovieByTitle(newMovie.getTitle())).thenReturn(resultByTitle);
            movieService.insert(newMovie);

            Mockito.verify(movieRepository).save(newMovie);

        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void insertNewMovieAddingToOtherMovie(){
        Movie newMovie = new Movie(38L, "Lord of the Rings", "Rowling", 1, 1);
        Movie resultByTitle = null;

        try {
            Mockito.when(movieService.getMovieByTitle(newMovie.getTitle())).thenReturn(newMovie);
            movieService.insert(newMovie);

            Mockito.verify(movieRepository).save(newMovie);

        } catch (BusinessException e) {
            e.printStackTrace();
        }


    }


}