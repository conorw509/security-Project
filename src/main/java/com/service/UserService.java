package com.service;

import com.model.Movies;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service("userService")
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Boolean findUserByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            return jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                    preparedStatement.setString(1, email);
                    ResultSet rs;
                    rs = preparedStatement.executeQuery();

                    if (rs.next()) {
                        return true;
                    } else {

                        return false;
                    }
                }
            });
        } catch (Exception e) {

        }
        return true;
    }


    public User saveUser(User u) {
        try {
            String query = "insert into users(name,last_name,email,password,repassword,authority) values(?,?,?,?,?,?)";

            jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
                        throws SQLException, DataAccessException {

                    preparedStatement.setString(1, u.getName());
                    preparedStatement.setString(2, u.getLastName());
                    preparedStatement.setString(3, u.getEmail());
                    preparedStatement.setString(4, (bCryptPasswordEncoder.encode(u.getPassword())));
                    preparedStatement.setString(5, (bCryptPasswordEncoder.encode(u.getRepassword())));
                    preparedStatement.setString(6, "ADMIN");
                    return preparedStatement.execute();

                }
            });
        } catch (Exception e) {

        }
        return u;
    }

    public Boolean update(User u) {
        try {
            String next = "insert into roles(auth) values(?)";

            return jdbcTemplate.execute(next, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
                        throws SQLException, DataAccessException {

                    preparedStatement.setString(1, "yes");

                    return preparedStatement.execute();

                }
            });
        } catch (Exception e) {

        }
        return true;
    }

    public List<Movies> findMovies(String name) {

        ArrayList<Movies> moviesList = new ArrayList<Movies>();
        String query = " select * from movies WHERE CONCAT(title,year,movieLength,movieLanguage)LIKE '%" + name + "%'";
        try {
            jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                    ResultSet rs;
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        Movies u = new Movies();
                        u.setTitle(rs.getString("title"));
                        u.setYear(rs.getInt("year"));
                        u.setMovieLength(rs.getInt("movieLength"));
                        u.setMovieLanguage(rs.getString("movieLanguage"));
                        moviesList.add(u);
                    }

                    return true;
                }
            });
        } catch (Exception e) {

        }
        return moviesList;
    }

    private static java.sql.Date getCurrentDate() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }
}
