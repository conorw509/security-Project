package com.service;

import com.model.User;
//import com.repository.RoleRepository;
//import com.repository.UserRepository;
import hash.PasswordEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RoleRepository roleRepository;

//    private UserRepository userRepository;
//    private RoleRepository roleRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    public UserService(UserRepository userRepository,
//                       RoleRepository roleRepository, JdbcTemplate jdbcTemplate,
//                       BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.jdbcTemplate = jdbcTemplate;
//    }

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
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setRepassword(bCryptPasswordEncoder.encode(user.getRepassword()));
//        user.setActive(1);
//        Role userRole = roleRepository.findByRole("ADMIN");
//        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
//        return userRepository.save(user);


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
//                Role userRole = roleRepository.findByRole("ADMIN");
//                u.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
//                userRepository.save(u);

                return preparedStatement.execute();

            }
        });
    }
}