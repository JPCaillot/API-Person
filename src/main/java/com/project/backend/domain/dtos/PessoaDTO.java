package com.project.backend.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO {
    private Integer idPessoa;
    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;
    @NotBlank(message = "CPF é um campo obrigatório")
    @Length(min = 11, max = 11)
    private String cpf;
    @NotNull
    @Past(message = "A data de nascimento não pode ser maior que a atual")
    private LocalDate nascimento;
    @Valid
    private EnderecoDTO endereco;
}
