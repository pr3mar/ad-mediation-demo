package com.demo.ad.mediation.controllers;

import com.demo.ad.mediation.models.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;

@Slf4j
public abstract class AbstractController {

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> internalServerError(EntityNotFoundException e) {
        log.error("Entity not found exception occurred", e);
        ExceptionResponse response = new ExceptionResponse(404, "Entity not found :(");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, DataIntegrityViolationException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> nullPointerHandler(Exception e) {
        log.error("Bad input exception occurred" + e.getMessage());
        final ExceptionResponse response;
        if (e instanceof DataIntegrityViolationException) {
            response = new ExceptionResponse(400, "Check your input. Network with provided ID already exists.");
        } else {
            response = new ExceptionResponse(400, "Check your input. " + e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> internalServerError(RuntimeException e) {
        log.error("Unexpected exception occurred", e);
        ExceptionResponse response = new ExceptionResponse(500, "Something went wrong :(");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
