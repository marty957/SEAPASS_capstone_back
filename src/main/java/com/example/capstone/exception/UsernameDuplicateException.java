package com.example.capstone.exception;

public class UsernameDuplicateException extends RuntimeException {
    public UsernameDuplicateException(String message) {
        super(message);
    }
}
