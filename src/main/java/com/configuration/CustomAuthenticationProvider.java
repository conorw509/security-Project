//package com.configuration;
//
//import com.model.User;
//import com.service.UserService;
//import org.jboss.aerogear.security.otp.Totp;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
//
//    private User user1;
//
//    @Autowired
//    private UserService userRepository;
//
//
//    @Override
//    public Authentication authenticate(Authentication auth) throws AuthenticationException {
//        String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
//        boolean user = userRepository.findUserByEmail(auth.getName());
//        if ((user != true)) {
//            throw new BadCredentialsException("Invalid email or password");
//        }
//
//        user1 = new User();
//        if (user1.isUsing2FA()) {
//            Totp totp = new Totp(user1.getSecret());
//            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
//                throw new BadCredentialsException("Invalid verfication code");
//            }
//        }
//
//        Authentication result = super.authenticate(auth);
//        return new UsernamePasswordAuthenticationToken(
//                user, result.getCredentials(), result.getAuthorities());
//    }
//
//    private boolean isValidLong(String code) {
//        try {
//            Long.parseLong(code);
//        } catch (NumberFormatException e) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}