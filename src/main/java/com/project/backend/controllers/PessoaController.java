package com.project.backend.controllers;

import com.project.backend.domain.dtos.PessoaDTO;
import com.project.backend.service.IPessoaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backend/pessoa")
@Validated
public class PessoaController {
    private final IPessoaService pessoaService;

    public PessoaController(final IPessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaDTO> createPessoa(@Valid @RequestBody @NotNull PessoaDTO pessoaDTO) {
        PessoaDTO pessoa = pessoaService.createPessoa(pessoaDTO);
        return new ResponseEntity<>(pessoa, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<PessoaDTO> updatePessoa(@Valid @RequestBody @NotNull PessoaDTO pessoaDTO) {
        PessoaDTO pessoa = pessoaService.updatePessoa(pessoaDTO);
        return new ResponseEntity<>(pessoa, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<PessoaDTO>> listPessoas() {
        List<PessoaDTO> pessoas = pessoaService.listPessoas();
        return new ResponseEntity<>(pessoas, HttpStatus.OK);
    }

    @DeleteMapping("/{idPessoa}")
    public void deletePessoa(@PathVariable @NotNull Integer idPessoa) {
        pessoaService.deletePessoa(idPessoa);
    }

    @GetMapping("/cpfs")
    public ResponseEntity<Map<String, String>> getNomeByCPF(@Valid @RequestParam List<String> cpfs) {
        Map<String, String> nomeCadastrado = pessoaService.getNomeByCPF(cpfs);
        return new ResponseEntity<>(nomeCadastrado, HttpStatus.OK);
    }
}
