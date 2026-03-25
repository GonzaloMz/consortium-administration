package com.consortium.admin.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("A person with email '" + email + "' already exists");
    }
}
