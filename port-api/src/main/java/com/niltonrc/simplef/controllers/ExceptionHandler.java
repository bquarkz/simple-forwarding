package com.niltonrc.simplef.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandler
{
    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler( Exception.class )
    @ResponseStatus( HttpStatus.NOT_FOUND )
    String someProblem( Exception ex )
    {
        return ex.getMessage();
    }
}
