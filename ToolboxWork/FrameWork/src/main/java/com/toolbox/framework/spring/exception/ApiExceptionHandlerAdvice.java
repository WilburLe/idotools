package com.toolbox.framework.spring.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@ControllerAdvice
public class ApiExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public String exception(Exception e) {
//        e.printStackTrace();
        return "error/500";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public JSON noHandlerFoundException(Exception e) {
//        e.printStackTrace();
        JSONObject result = new JSONObject();
        JSONObject error = new JSONObject();
        error.put("error", "404");
        result.put("error", error);
        result.put("status", 404);
        return result;
    }

}
