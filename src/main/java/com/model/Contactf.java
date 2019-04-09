package com.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Contactf {

    @NotEmpty
    private String name;
    @NotEmpty
    private String lname;
    @NotEmpty
    @Size(max = 500, message = "Max 500 character")
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
