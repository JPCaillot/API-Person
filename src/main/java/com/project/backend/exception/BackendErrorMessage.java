package com.project.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class BackendErrorMessage {
    private HttpStatusCode status;
    private List<String> errors;

    public BackendErrorMessage(HttpStatusCode status, List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public BackendErrorMessage(HttpStatusCode status, String error) {
        super();
        this.status = status;
        errors = Collections.singletonList(error);
    }
}
