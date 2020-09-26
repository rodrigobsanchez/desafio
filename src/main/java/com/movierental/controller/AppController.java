package com.movierental.controller;

import com.movierental.model.Movie;

import com.movierental.model.User;
import com.movierental.service.MovieService;
import com.movierental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/newUser")
    public String showNewProductForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "new_user";
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    @ResponseBody
    public String saveUser(@ModelAttribute("user") User user) {
        return userService.insertUser(user);
    }
}
