package com.project.apiperson.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class PersonAPIErrorMessage {
    private HttpStatusCode status;
    private List<String> errors;

    public PersonAPIErrorMessage(HttpStatusCode status, List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public PersonAPIErrorMessage(HttpStatusCode status, String error) {
        super();
        this.status = status;
        errors = Collections.singletonList(error);
    }
}
