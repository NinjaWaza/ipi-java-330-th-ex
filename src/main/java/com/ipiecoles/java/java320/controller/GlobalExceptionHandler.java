package com.ipiecoles.java.java320.controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.concurrent.ExecutionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("erreur");
        modelAndView.addObject("errorMsg", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException e){
        ModelAndView modelAndView = new ModelAndView("erreur");
        modelAndView.addObject("errorMsg", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleEntityExistsException(EntityExistsException e){
        ModelAndView modelAndView = new ModelAndView("erreur");
        modelAndView.addObject("errorMsg", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        ModelAndView modelAndView = new ModelAndView("erreur");
        modelAndView.addObject("errorMsg", "La valeur " + e.getValue() + " pour le paramètre " + e.getName() + " est incorrecte ! ");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleAllException(Exception e){
        ModelAndView modelAndView = new ModelAndView("erreur");
        modelAndView.addObject("errorMsg", "Une erreur technique est survenue");
        return modelAndView;
    }
}