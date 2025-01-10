package com.project.backend.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Endereco {
    @Id
    private Integer idPessoa;
    @OneToOne
    @MapsId
    @JoinColumn(name = "idPessoa")
    private Pessoa pessoa;
    private Integer cep;
    private String rua;
    private Integer numero;
    private String cidade;
    private String estado;
}
