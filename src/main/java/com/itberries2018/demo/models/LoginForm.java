package com.itberries2018.demo.models;

@SuppressWarnings("unused")
public class LoginForm {

    private String login;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LoginForm(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public LoginForm() {
    }
}
