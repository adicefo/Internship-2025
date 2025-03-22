package com.example.internship_api.exception;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException() {
        super("Password and confirm password do not match. Please provide equal passwords ");
    }
}
