package com.movierental.controller;

import com.movierental.MyUserDetails;
import com.movierental.exception.BusinessException;
import com.movierental.model.Movie;

import com.movierental.model.User;
import com.movierental.service.MovieService;
import com.movierental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class AppController {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;


    @RequestMapping("/")
    public String viewHomePage(Model model) {
        List<Movie> listMovies = movieService.getAll();
        model.addAttribute("listMovies", listMovies);

        return "index";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:/login?logout";
    }
    @RequestMapping("/newUser")
    public String showNewUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "new_user";
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    @ResponseBody
    public String saveUser(@ModelAttribute("user") User user) throws BusinessException {
        return userService.insertUser(user);
    }

    @RequestMapping(value = "/insertUser" , method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String insertUser(@RequestParam("name") String name, @RequestParam("username") String username, @RequestParam("password") String password) throws BusinessException {
        return userService.insertUser(new User(name, username, password));
    }

    @RequestMapping("/deleteUser")
    public String deleteUser() throws BusinessException {
        String currentUserLoggedIn = getLoggedInUsername();
        if(Objects.isNull(currentUserLoggedIn)){
            return "403";
        }else{
            userService.deleteUser(currentUserLoggedIn);
            return "redirect:/login?logout";
        }
    }

    @RequestMapping("/allMovies")
    @ResponseBody
    public List<Movie> viewAllMovies() {
       return movieService.getAll();
    }

    @RequestMapping("/allAvailableMovies")
    @ResponseBody
    public List<Movie> getAllAvailableMovies() {
        return movieService.getAllAvailableMovies();
    }

    @RequestMapping("/myMovies")
    @ResponseBody
    public List<String> getUserMovies() throws BusinessException {
        List<String> list = new ArrayList<>();
        String currentUserLoggedIn = getLoggedInUsername();
        if (!Objects.isNull(currentUserLoggedIn)) {
            list = movieService.getUserMovies(currentUserLoggedIn);
        }
        return list;
    }

    @RequestMapping("/searchByTitle")
    @ResponseBody
    public String getMovieByTitle(String title) throws BusinessException {
        Movie movie = movieService.getMovieByTitle(title);
        if(Objects.isNull(movie)){
            return "Nenhum filme foi encontrado com esse nome: " + title;
        }else{
            return movie.toString();
        }
    }

    @RequestMapping(value = "/rentMovie" , method = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public String updateRentMovie(String moviename) throws BusinessException {
        String currentUserLoggedIn = getLoggedInUsername();
        if(!Objects.isNull(currentUserLoggedIn)){
            Movie movie = movieService.getMovieByTitle(moviename);
            User user = userService.findByUserName(currentUserLoggedIn);

            if(movie.getAvailable() > 0){
                movie.setAvailable(movie.getAvailable() - 1);
                movieService.rentMovie(movie, user);
                return "O filme "+ movie.getTitle() + " foi alugado pelo cliente " + user.getName() + ". Aproveite!";
            }else {
                return  movie.getTitle() + " não está disponível no momento.";
            }
        }else{
            return "Problema na sessão fazer o login novamente";
        }

    }

    @RequestMapping(value = "/giveMovieBack" , method = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public String updateMovieAvailable(String moviename) throws BusinessException {
        String currentUserLoggedIn = getLoggedInUsername();
        Movie movie = movieService.getMovieByTitle(moviename);
        User user = userService.findByUserName(currentUserLoggedIn);
        if(movie.getAvailable() < movie.getAmount()){
            movieService.giveMovieBack(movie, user);
            return "O filme "+ movie.getTitle() + " foi devolvido com sucesso!";
        }else {
            return "Esse filme já foi devolvido.";
        }

    }

   /* Method to get current username loggedIn*/
    public String getLoggedInUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
           return currentUserName;
        }
        return null;
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
        return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
