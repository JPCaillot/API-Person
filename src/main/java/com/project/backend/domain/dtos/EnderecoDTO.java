package com.project.backend.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {
    @NotNull
    private Integer cep;
    @NotBlank
    private String rua;
    private Integer numero;
    @NotBlank
    private String cidade;
    @NotBlank
    private String estado;
}
