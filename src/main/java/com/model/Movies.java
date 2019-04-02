package com.model;


public class Movies {

    private int id;
    private String title;
    private int year;
    private int movieLength;
    private String movieLanguage;


    public Movies(String title, int year, int movieLength, String movieLanguage) {
        this.title = title;
        this.year = year;
        this.movieLength = movieLength;
        this.movieLanguage = movieLanguage;
    }

    public Movies() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public String getMovieLanguage() {
        return movieLanguage;
    }

    public void setMovieLanguage(String movieLanguage) {
        this.movieLanguage = movieLanguage;
    }
}
