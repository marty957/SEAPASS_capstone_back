package com.example.capstone.exception;

public class CertificateNotFound extends RuntimeException {
    public CertificateNotFound(String message) {
        super(message);
    }
}
