package com.consortium.admin.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(Long id) {
        super("Person not found with id: " + id);
    }

    public PersonNotFoundException(String message) {
        super(message);
    }
}
