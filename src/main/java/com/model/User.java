package com.model;

import validator.FieldMatch;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@FieldMatch(first = "repassword", second = "password", message = "The password fields must match")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @NotEmpty(message = "Please provide a valid Password")
    @Size(min = 8, max = 15, message = "*Password must be between 8 and 15 characters")
    @Pattern(regexp = "(.*[A-Z].*)", message = "*Password must contain at Least 1 uppercase Letter")
    @Pattern(regexp = "(.*[a-z].*)", message = "*Password must contain at Least 1 lowercase Letter")
    @Pattern(regexp = "(.*[0-9].*)", message = "*Password must contain at Least 1 number")
    private String password;
    @NotEmpty
    private String repassword;
    @NotEmpty(message = "*Please provide your name")
    @Size(min = 5, max = 15, message = "*Name must be between 5 and 15 characters")
    private String name;
    @NotEmpty(message = "*Please provide your last name")
    @Size(min = 5, max = 20, message = "*Last Name must be between 5 and 20 characters")
    private String lastName;
    private String role;
    private boolean isEnabled;
    
    public User() {
        super();
        this.isEnabled = false;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
