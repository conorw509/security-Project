package com.controller;

import javax.validation.Valid;

import com.model.ConfirmationToken;
import com.model.Contactf;
import com.model.Movies;
import com.model.User;
import com.service.UserService;
import com.service.emailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Locale;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    MessageSource messages;

//    @Autowired
//    private IUserService service;

//    @Autowired
//    private JavaMailSender javaMailSender;

//    @Autowired
//    private UserRepository userRepository;

//    @Autowired
//    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private emailService emailSenderService;

    @Autowired
    ApplicationEventPublisher eventPublisher;


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
    public ModelAndView contactPost(
                                    @Valid Contactf user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("contact");
        } else {
            modelAndView.addObject("contactf", new Contactf());
            modelAndView.setViewName("contact");
        }
        return modelAndView;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@RequestParam("email") String email,
                                      @Valid User user, BindingResult bindingResult, WebRequest request) {
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
            user.setEnabled(true);
            User registered = userService.saveUser(user);
            userService.update(user);


//            try {
//                String appUrl = request.getContextPath();
//                eventPublisher.publishEvent(new OnRegistrationCompleteEvent
//                        (registered, request.getLocale(), appUrl));
//            } catch (Exception me) {
            //  return new ModelAndView("emailError", "user", user);
//            }

            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            //   confirmationTokenRepository.save(confirmationToken);
            userService.saveConfrimationToken(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("chand312902@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8090/confirm-account?token=" + confirmationToken.getConfirmationToken());

//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setFrom(user.getEmail());
//            mailMessage.setTo("rc@feedback.com");
//            mailMessage.setSubject("New feedback from " + user.getName());
//            mailMessage.setText(confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("emailId", user.getEmail());
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }


//    @RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
//    public String confirmRegistration
//            (WebRequest request, Model model, @RequestParam("token") String token) {
//
//        Locale locale = request.getLocale();
//
//        ConfirmationToken verificationToken = userService.findConfirmationToken(token);
//        if (verificationToken == null) {
//            String message = messages.getMessage("auth.message.invalidToken", null, locale);
//            model.addAttribute("message", message);
//            return "redirect:/badUser.html?lang=" + locale.getLanguage();
//        }
//        User user = verificationToken.getUser();
//        Calendar cal = Calendar.getInstance();
//        if ((verificationToken.getCreatedDate().getTime() - cal.getTime().getTime()) <= 0) {
//            String messageValue = messages.getMessage("auth.message.expired", null, locale);
//            model.addAttribute("message", messageValue);
//            return "redirect:/badUser.html?lang=" + locale.getLanguage();
//        }
//
//        user.setEnabled(true);
//        return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
//    }


//    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
//    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {
////
////        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
//
//        boolean foundToken = userService.findConfirmationToken(confirmationToken);
//
//        if (foundToken != false) {
//            User user = new User();
//            ConfirmationToken token = new ConfirmationToken(user);
//            Boolean user1 = userService.findUserByEmail(token.getUser().getEmail());
//            if (user1) {
//                user.setEnabled(true);
//                userService.saveUser(user);
//                modelAndView.setViewName("accountVerified");
//            }
//        } else {
//            modelAndView.addObject("message", "The link is invalid or broken!");
//            modelAndView.setViewName("error");
//        }
//
//        return modelAndView;
//    }


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
