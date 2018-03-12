package com.itberries2018.demo.models;

@SuppressWarnings("unused")
public class LoginForm {

    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LoginForm(String emal, String password) {
        this.email = emal;
        this.password = password;
    }

    public LoginForm() {
    }
}
