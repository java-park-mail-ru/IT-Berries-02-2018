package com.itberries2018.demo.models;


public class LoginForm {

    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LoginForm(String emal, String password) {
        this.email = emal;
        this.password = password;
    }
}
