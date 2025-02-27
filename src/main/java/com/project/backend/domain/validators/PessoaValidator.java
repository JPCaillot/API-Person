package com.project.backend.domain.validators;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PessoaValidator {
    private static final String STARTS_WITH_CAPITAL = "[A-ZÁÉÍÓÚÃÕ][a-záéíóúãõ]+";
    private static final String SPACES = "\\s+";

    public static void validateNome(String nome) {
        log.info("Validando dados cadastrais de {}", nome);

        String[] partes = nome.trim().split(SPACES);
        if (partes.length < 2) {
            throw new IllegalArgumentException("O nome no cadastro deve conter mais de um nome");
        }

        for (String parte : partes) {
            if (!parte.matches(STARTS_WITH_CAPITAL)) {
                throw new IllegalArgumentException("A primeira letra de cada nome deve ser maiúscula, e as demais minúsculas");
            }
        }
        log.info("Validações de nome de {} concluídas", nome);
    }
}
