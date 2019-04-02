package com.controller;

import javax.validation.Valid;

import com.model.Movies;
import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@RequestParam("email") String email,
                                      @Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        user.setEmail(email);
        boolean userExists = userService.findUserByEmail(user);
        if (userExists) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            userService.update(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home(
//    @RequestParam("title") String title,
//                             @RequestParam("year") String year,
//                             @RequestParam("movieLength") String movieLength,
//                             @RequestParam("movieLanguage") String movieLanguage,
                             @Valid Movies movies,
                             Model model){


//        movies.setTitle(title);
//        movies.setYear(year);
//        movies.setMovieLength(movieLength);
//        movies.
        ModelAndView modelAndView = new ModelAndView();
        model.addAttribute(userService.findMovies());
       // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("adminMessage", "Please Search Content");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }


}
