package com.project.backend.domain.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PessoaValidatorTest {
    @Test
    void givenValidName_whenValidateName_thenDoesNotThrowException() {
        assertDoesNotThrow(() -> PessoaValidator.validateNome("João Silva"));
    }

    @Test
    void givenInvalidSingleName_whenValidateName_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PessoaValidator.validateNome(" João "));
    }

    @Test
    void givenInvalidLowerCaseName_whenValidateName_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PessoaValidator.validateNome("João da Silva"));
    }
}
