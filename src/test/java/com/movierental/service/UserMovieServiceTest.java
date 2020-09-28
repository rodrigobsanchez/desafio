package com.movierental.service;

import com.movierental.exception.BusinessException;
import com.movierental.model.Movie;
import com.movierental.model.UserMovie;
import com.movierental.repository.UserMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMovieServiceTest {

    @InjectMocks
    private UserMovieService userMovieService;

    @Mock
    private UserMovieRepository userMovieRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAll() {
        List<UserMovie> list = Arrays.asList(
                new UserMovie(1L, 35L, "rodrigo", "Seven" ),
                new UserMovie(1L, 35L, "rodrigo", "Avatar" ),
                new UserMovie(1L, 45L, "teste", "StarWars" ),
                new UserMovie(1L, 45L, "teste", "Unknown" ));

        Mockito.when(userMovieService.getAll()).thenReturn(list);

        List<UserMovie> answer = userMovieService.getAll();

        assertEquals(list, answer);
    }

    @Test
    void insertUserMovieIsNull() {
        UserMovie b = null;
        try{
            userMovieService.insertUserMovie(b);
            fail("Deveria ter lançado exceção!");
        } catch (BusinessException e) {
            assertEquals("Object UserMovie is Null", e.getMessage());
        }
    }
    @Test
    void insertUserMovie() {
        UserMovie a = new UserMovie(1L, 35L, "rodrigo", "Seven" );
        try{
            userMovieService.insertUserMovie(a);
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        Mockito.verify(userMovieRepository).save(a);


    }
}