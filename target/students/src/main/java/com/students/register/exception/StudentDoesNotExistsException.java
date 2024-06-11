package com.students.register.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentDoesNotExistsException extends RuntimeException {
    public StudentDoesNotExistsException(String message) {
        super(message);
    }
}
