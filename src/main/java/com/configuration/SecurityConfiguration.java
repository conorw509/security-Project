package com.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                .jdbcAuthentication()
                .usersByUsernameQuery("SELECT email, password, enabled " +
                        "FROM users " +
                        "WHERE email = ?")
                .authoritiesByUsernameQuery("select email AS username, authority AS authority from users u,roles r where (r.role_id=u.user_id) AND email=?")
                .dataSource(dataSource)
                .rolePrefix("ROLE_")
                .passwordEncoder(bCryptPasswordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        try {
            http.
                    authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/registration").permitAll()
                    .antMatchers("/confirm").permitAll()
                    .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                    .authenticated().and().csrf().disable().formLogin()
                    //.authenticationDetailsSource(authenticationDetailsSource)
                    .loginPage("/login").failureUrl("/login?error=true")
                    .defaultSuccessUrl("/home")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .and().logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/").and().exceptionHandling()
                    .accessDeniedPage("/access-denied");
        } catch (Exception e) {
            System.out.println("Something happened");
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
