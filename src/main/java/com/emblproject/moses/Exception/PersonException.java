package com.emblproject.moses.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PersonException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public PersonException(String message) {
        super(message);
    }
}
