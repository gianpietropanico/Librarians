package com.example.thelibrariansapp.models;

public class User {

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public User( String username) {
        this.username = username;
    }


    private String username;
    private String password;
}
