package com.controller;

import javax.validation.Valid;

import com.model.ConfirmationToken;
import com.model.Movies;
import com.model.User;
import com.model.emailCfg;
import com.service.UserService;
import com.service.emailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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

    private com.model.emailCfg emailCfg;

//    @Autowired
//    private UserRepository userRepository;

//    @Autowired
//    private ConfirmationTokenRepository confirmationTokenRepository;

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

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@RequestParam("email") String email,
                                      @Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        // user.setEmail(email);
        boolean userExists = userService.findUserByEmail(email);
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
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            // confirmationTokenRepository.save(confirmationToken);
            userService.saveConfrimationToken(confirmationToken);

            emailCfg = new emailCfg();
            // Create a mail sender
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//            mailSender.setHost(emailCfg.getHost());
//            mailSender.setPort(emailCfg.getPort());
//            mailSender.setUsername(emailCfg.getUsername());
//            mailSender.setPassword(emailCfg.getPassword());


//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(user.getEmail());
//            mailMessage.setSubject("Complete Registration!");
//            mailMessage.setFrom("chand312902@gmail.com");
//            mailMessage.setText("To confirm your account, please click here : "
//                    + "http://localhost:8082/confirm-account?token=" + confirmationToken.getConfirmationToken());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(user.getEmail());
            mailMessage.setTo("rc@feedback.com");
            mailMessage.setSubject("New feedback from " + user.getName());
            mailMessage.setText(confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("emailId", user.getEmail());

//            modelAndView.setViewName("successfulRegisteration");
            modelAndView.addObject("successMessage", "User has been registered successfully");
//            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {
//
//        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        boolean foundToken = userService.findConfirmationToken(confirmationToken);

        if (foundToken != false) {
            User user = new User();
            ConfirmationToken token = new ConfirmationToken(user);
            Boolean user1 = userService.findUserByEmail(token.getUser().getEmail());
            if (user1) {
                user.setEnabled(true);
                userService.saveUser(user);
                modelAndView.setViewName("accountVerified");
            }
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
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
            Model model) {


//        movies.setTitle(title);
//        movies.setYear(year);
//        movies.setMovieLength(movieLength);
//        movies.
        ModelAndView modelAndView = new ModelAndView();
        //model.addAttribute(userService.findMovies());
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("adminMessage", "Please Search Content");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }


}
