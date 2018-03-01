package com.itberries_2018.demo.services;

public class CustomErrorType {
    private final String errorMessage;

    public CustomErrorType(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
