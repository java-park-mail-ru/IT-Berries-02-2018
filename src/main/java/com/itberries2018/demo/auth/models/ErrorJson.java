package com.itberries2018.demo.auth.models;

public class ErrorJson {
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ErrorJson(String error) {
        this.error = error;
    }
}
