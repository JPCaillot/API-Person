package com.project.apiperson.exception;

import org.hibernate.ObjectNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class PersonAPIExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        PersonAPIErrorMessage personAPIErrorMessage = new PersonAPIErrorMessage(status, errors);

        return new ResponseEntity<>(personAPIErrorMessage, personAPIErrorMessage.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        PersonAPIErrorMessage personAPIErrorMessage = new PersonAPIErrorMessage(HttpStatusCode.valueOf(400), ex.getMessage());

        return new ResponseEntity<>(personAPIErrorMessage, new HttpHeaders(), personAPIErrorMessage.getStatus());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleObjectNotFound(
            ObjectNotFoundException ex, WebRequest request) {

        PersonAPIErrorMessage personAPIErrorMessage = new PersonAPIErrorMessage(HttpStatusCode.valueOf(404), ex.getMessage());

        return new ResponseEntity<>(personAPIErrorMessage, new HttpHeaders(), personAPIErrorMessage.getStatus());
    }
}
