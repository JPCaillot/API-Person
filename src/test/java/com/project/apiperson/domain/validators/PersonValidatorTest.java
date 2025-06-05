package com.project.apiperson.domain.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonValidatorTest {
    @Test
    void givenValidName_whenValidateName_thenDoesNotThrowException() {
        assertDoesNotThrow(() -> PersonValidator.validateName("João Silva"));
    }

    @Test
    void givenInvalidSingleName_whenValidateName_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PersonValidator.validateName(" João "));
    }

    @Test
    void givenInvalidLowerCaseName_whenValidateName_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PersonValidator.validateName("João da Silva"));
    }
}
