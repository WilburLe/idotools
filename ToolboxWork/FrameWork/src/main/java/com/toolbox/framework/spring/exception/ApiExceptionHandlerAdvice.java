package com.toolbox.framework.spring.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ApiExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public String exception(Exception e) {
        e.printStackTrace();
        return "error/500";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String noHandlerFoundException(Exception e) {
        e.printStackTrace();
        return "error/404";
    }

}
