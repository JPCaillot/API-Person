package com.project.backend.services;

import com.project.backend.domain.dtos.PessoaDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public interface IPessoaService {
    PessoaDTO createPessoa(PessoaDTO pessoa);
    PessoaDTO updatePessoa(PessoaDTO pessoa);
    List<PessoaDTO> listPessoas();
    void deletePessoa(Integer idPessoa);
    Map<String, String> getNomeByCPF(@Valid @NotBlank List<String> cpfs);
}
