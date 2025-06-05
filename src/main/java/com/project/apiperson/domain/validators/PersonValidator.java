package com.project.apiperson.domain.validators;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonValidator {
    private static final String STARTS_WITH_CAPITAL = "[A-ZÁÉÍÓÚÃÕ][a-záéíóúãõ]+";
    private static final String SPACES = "\\s+";
    private PersonValidator() {}

    public static void validateName(String name) {
        log.info("Validating data from {}", name);

        String[] parts = name.trim().split(SPACES);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Name should contain more than one part");
        }

        for (String parte : parts) {
            if (!parte.matches(STARTS_WITH_CAPITAL)) {
                throw new IllegalArgumentException(
                        "First letter of each name should be in upper case, and the remaining ones in lower case"
                );
            }
        }
        log.info("Name validations of {} are finished", name);
    }
}
