package com.service;

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


@Service("userService")
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    public Boolean findUserByEmail(User email) {
        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            return jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                    preparedStatement.setString(1, email.getEmail());
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


    public Boolean saveUser(User u) {
        try {
            String query = "insert into users(name,last_name,email,password,repassword,authority) values(?,?,?,?,?,?)";

            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
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
        return true;
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
//
//    public Boolean findMovies(Movies u) {
//
//        ArrayList<Movies> moviesList = new ArrayList<Movies>();
//
//        ResultSet resultset = statement.executeQuery(sql)
//        String query = " SELECT * from movies where title=? and year=? and movieLength=? and movieLanguage=?";
//        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
//            @Override
//            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
//
//                preparedStatement.setString(1, u.getTitle());
//                preparedStatement.setInt(2, u.getYear());
//                preparedStatement.setInt(3, u.getMovieLength());
//                preparedStatement.setString(4, u.getMovieLanguage());
//                ResultSet rs;
//                rs = preparedStatement.executeQuery();
//                while (rs.next()) {
//                    moviesList.add(new Movies(rs.getString("title"), rs.getInt("year"), rs.getInt("movieLength"), rs.getString("movieLanguage")));
//                }
//                return true;
//
//            }
//        });
//    }

}
