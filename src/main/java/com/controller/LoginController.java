package com.controller;

import javax.validation.Valid;

import com.model.Contactf;
import com.model.User;
import com.service.UserService;
import com.service.emailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    private emailService emailSenderService;

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


    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public ModelAndView contact() {
        ModelAndView modelAndView = new ModelAndView();
        Contactf user = new Contactf();
        modelAndView.addObject("contactf", user);
        modelAndView.setViewName("contact");
        return modelAndView;
    }


    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public ModelAndView contactPost(@RequestParam("input") String input,
                                    @RequestParam("name") String name,
                                    @RequestParam("lname") String subject,
                                    @Valid Contactf con, BindingResult bindingResult,Principal principal) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("contact");

        } else {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(name);
                mailMessage.setTo("secureApp@mail.com");
                mailMessage.setSubject(subject);
                mailMessage.setText(input);
                mailMessage.setCc(name);
              //  boolean emailFound = userService.findUserByEmail(name);
               String currUser = principal.getName();
                if(name.equals(currUser)) {
                    boolean sent = emailSenderService.sendEmail(mailMessage);

                    if (sent) {
                        modelAndView.addObject("successMessage", "Email Sent!");
                        modelAndView.addObject("contactf", new Contactf());
                        modelAndView.setViewName("contact");
                    }
                    modelAndView.addObject("contactf", new Contactf());
                    modelAndView.setViewName("contact");
                }else{
                    modelAndView.addObject("successMessage", "Please Provide Your Account email!");
                    modelAndView.addObject("contactf", new Contactf());
                    modelAndView.setViewName("contact");
                }
            } catch (Exception e) {
                modelAndView.addObject("errormsg", "Email did not send!");
                modelAndView.addObject("contactf", new Contactf());
                modelAndView.setViewName("contact");
            }
        }
        return modelAndView;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@RequestParam("email") String email,
                                      @Valid User user, BindingResult bindingResult, WebRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        boolean userExists = userService.findUserByEmail(email);
        if (userExists) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            user.setEnabled(true);
            userService.saveUser(user);
            userService.update(user);

            modelAndView.addObject("emailId", user.getEmail());
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Map<String, Object> map, @RequestParam(defaultValue = "") String name) {
        map.put("moviesList", userService.findMovies(name));
        return "admin/home";
    }
}
