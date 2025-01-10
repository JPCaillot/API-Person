package com.project.backend.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPessoa;
    private String nome;
    private String cpf;
    private LocalDate nascimento;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private Endereco endereco;
}
