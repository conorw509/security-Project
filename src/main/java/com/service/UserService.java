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
        String sql = "SELECT * FROM user1 WHERE email = ?";
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
    }

    public Boolean saveUser(User u) {

        String query = "insert into user1(name,last_name,email,password,repassword,authority) values(?,?,?,?,?,?)";

        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
                    throws SQLException, DataAccessException {

                preparedStatement.setString(1, u.getName());
                preparedStatement.setString(2, u.getLastName());
                preparedStatement.setString(3, u.getEmail());
                preparedStatement.setString(4,(bCryptPasswordEncoder.encode(u.getPassword())));
                preparedStatement.setString(5,(bCryptPasswordEncoder.encode(u.getRepassword())));
                preparedStatement.setString(6, "ADMIN");
                return preparedStatement.execute();

            }
        });
    }
}