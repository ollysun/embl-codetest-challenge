package com.emblproject.moses.Exception;

import org.springframework.security.core.AuthenticationException;

public class UnAuthorizedException extends AuthenticationException {
    public UnAuthorizedException(String message) { super(message); }
}