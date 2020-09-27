package com.movierental.controller;

import com.movierental.model.Movie;

import com.movierental.model.User;
import com.movierental.service.MovieService;
import com.movierental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public String saveUser(@ModelAttribute("user") User user) {
        return userService.insertUser(user);
    }

    @RequestMapping(value = "/insertUser" , method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String insertUser(@RequestParam("name") String name, @RequestParam("username") String username, @RequestParam("password") String password){
        return userService.insertUser(new User(name, username, password));
    }

    @RequestMapping("/deleteUser")
    public String deleteUser(String username){
        String currentUserLoggedIn = getLoggedInUsername();
        if(currentUserLoggedIn.equals(username)){
            userService.deleteUser(username);
            return "redirect:/login?logout";
        }
            return "403";
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
    public List<String> getUserMovies(String username) {
        List<String> list = new ArrayList<>();
        String currentUserLoggedIn = getLoggedInUsername();
        if (currentUserLoggedIn.equals(username)) {
            list = movieService.getUserMovies(username);
        }
        return list;
    }

    @RequestMapping("/searchByTitle")
    @ResponseBody
    public String getMovieByTitle(String title){
        Movie movie = movieService.getMovieByTitle(title);
        if(movie == null){
            return "Nenhum filme foi encontrado com esse nome: " + title;
        }else{
            return movie.toString();
        }
    }

    @RequestMapping(value = "/rentMovie" , method = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public String updateRentMovie(String username, String moviename){
        String currentUserLoggedIn = getLoggedInUsername();
        if(currentUserLoggedIn.equals(username)){
            Movie movie = movieService.getMovieByTitle(moviename);
            User user = userService.findByUserName(username);

            if(movie.getAvailable() > 0){
                movie.setAvailable(movie.getAvailable() - 1);
                movieService.rentMovie(movie, user);
                return "O filme "+ movie.getTitle() + " foi alugado pelo cliente " + user.getName() + ". Aproveite!";
            }else {
                return  movie.getTitle() + " não está disponível no momento.";
            }
        }else{
            return "Apenas ações para usuário que está logado! Usuário logado: "+ currentUserLoggedIn;
        }

    }

    @RequestMapping(value = "/giveMovieBack" , method = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public String updateMovieAvailable(String username, String moviename){
        Movie movie = movieService.getMovieByTitle(moviename);
        User user = userService.findByUserName(username);
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
        return "";
    }


}
