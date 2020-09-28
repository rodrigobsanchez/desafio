package com.movierental.service;

import com.movierental.exception.BusinessException;
import com.movierental.model.User;
import com.movierental.repository.UserMovieRepository;
import com.movierental.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMovieRepository userMovieRepository;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void insertUserParameterNull() {
        User user = null;
        try{
            userService.insertUser(user);
            fail("Deveria ter lançado exceção!");
        } catch (BusinessException e) {
            assertEquals("Objeto User do parametro newUser está null", e.getMessage());
        }
    }

    @Test
    void insertUserAlreadyExist(){
        User user = new User("Testador", "tester@tester.com", "123456", "ROLE_USER", true);
        user.setId(45L);
        String response = "O email " + user.getUsername() + " já está cadastrado. Inserir outro!";
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        try {
            String answer = userService.insertUser(user);
        } catch (BusinessException e) {
            assertEquals("O email " + user.getUsername() + " já está cadastrado. Inserir outro!", response);
        }
    }

    @Test
    void insertUserNoMatch(){
        User user = new User("Testador", "tester@tester.com", "123456", "ROLE_USER", true);
        user.setId(45L);
        String response = "Novo cliente Cadastrado! Nome: "+ user.getName() + " Email: " + user.getUsername() +".";
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        try {
            String answer = userService.insertUser(user);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        Mockito.verify(userRepository).save(user);
        assertEquals("Novo cliente Cadastrado! Nome: "+ user.getName() + " Email: " + user.getUsername() +".", response);
    }

    @Test
    void insertUserNoMatchInvalidName(){
        User user = new User("", "tester@tester.com", "123456", "ROLE_USER", true);
        User user2 = new User(null, "tester@tester.com", "123456", "ROLE_USER", true);
        user.setId(45L);
        String response = "O nome está vazio ou inválido.";

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        try {
            String answer = userService.insertUser(user);
            String anser2 = userService.insertUser(user);
            assertEquals(response, answer);
            assertEquals(response, anser2);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void insertUserNoMatchInvalidEmailOrUsername(){
        User user = new User("Testador", "tester", "123456", "ROLE_USER", true);
        user.setId(45L);
        String response = "Seu email não é válido";

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        try {
            String answer = userService.insertUser(user);
            assertEquals(response, answer);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void insertUserNoMatchInvalidPassword(){
        User user = new User("Testador", "tester@tester.com", null, "ROLE_USER", true);
        User user2 = new User("Testador", "tester@tester.com", "", "ROLE_USER", true);
        User user3 = new User("Testador", "tester@tester.com", "123aihdausihduiashduiashduiashdiasuhdsauidhasuidhiasuhdasiuhd456", "ROLE_USER", true);
        user.setId(45L);
        String response = "O password está irregular. Deve ser menos de 32 caracteres ou está vazio";

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        try {
            String answer = userService.insertUser(user);
            String answer2 = userService.insertUser(user);
            String answer3 = userService.insertUser(user);
            assertEquals(response, answer);
            assertEquals(response, answer2);
            assertEquals(response, answer3);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteUserParameterUserIsNull() {
        String username = null;
        try{
            userService.deleteUser(username);
            fail("Deveria ter lançado exceção!");
        } catch (BusinessException e) {
            assertEquals("Username is null, shouldn't be", e.getMessage());
        }
    }

    @Test
    void deleteUser() {
        String username = "teste@teste.com";
        User user = new User("Testador", "teste@teste.com", "blabla", "ROLE_USER", true);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);

        try {
            userService.deleteUser(username);
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        Mockito.verify(userRepository).delete(user);
    }

    @Test
    void findByUserName() {
        String username = "teste@teste.com";
        User user = new User("Testador", "teste@teste.com", "blabla", "ROLE_USER", true);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);

        User answer = userService.findByUserName(username);
        assertEquals(user, answer);
    }
}