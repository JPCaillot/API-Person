package com.project.backend.exception;

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
public class BackendExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        BackendErrorMessage backendErrorMessage = new BackendErrorMessage(status, errors);

        return new ResponseEntity<>(backendErrorMessage, backendErrorMessage.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        BackendErrorMessage backendErrorMessage = new BackendErrorMessage(HttpStatusCode.valueOf(400), ex.getMessage());

        return new ResponseEntity<>(backendErrorMessage, new HttpHeaders(), backendErrorMessage.getStatus());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleObjectNotFound(
            ObjectNotFoundException ex, WebRequest request) {

        BackendErrorMessage backendErrorMessage = new BackendErrorMessage(HttpStatusCode.valueOf(404), ex.getMessage());

        return new ResponseEntity<>(backendErrorMessage, new HttpHeaders(), backendErrorMessage.getStatus());
    }
}
