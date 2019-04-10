package com.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class Contactf implements Serializable {

    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String name;
    @NotEmpty(message = "*Please provide Subject")
    @Size(min = 3, max = 30, message = "*Subject must be between 3 and 30 characters")
    private String lname;
    @NotEmpty(message = "*Must not be Empty")
    @Size(max = 500, message = "*Max 500 character")
    private String input;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
