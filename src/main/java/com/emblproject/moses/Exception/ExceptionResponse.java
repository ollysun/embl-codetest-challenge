package com.emblproject.moses.Exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.*;

@Data
public class ExceptionResponse {

    private Date timestamp;
    private String message;
    private HttpStatus status;
    private List<String> errors;

    public ExceptionResponse(){
        super();
    }

    public ExceptionResponse(final HttpStatus status,final Date timestamp, final String message, final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public ExceptionResponse(final HttpStatus status, final Date timestamp, final String message) {
        super();
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
