package com.demo.ad.mediation.controllers;

import com.demo.ad.mediation.models.dto.ExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;

public abstract class AbstractController {

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> entityNotFoundHandler(EntityNotFoundException e) {
        e.printStackTrace();
        ExceptionResponse response = new ExceptionResponse(404, "Entity not found: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> dataIntegrityHandler(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(
            new ExceptionResponse(400, "Network with provided ID already exists."),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, DataIntegrityViolationException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> validationHandler(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(
            new ExceptionResponse(400, e.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return new ResponseEntity<>(
            new ExceptionResponse(500, "Something went wrong :("),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
