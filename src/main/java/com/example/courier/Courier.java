package com.example.courier;

public class Courier {

    String login;
    String password;
    String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Object setLogin(String login) {
        this.login = login;
        return this;
    }

    public Object setPassword(String password) {
        this.password = password;
        return this;
    }
}
