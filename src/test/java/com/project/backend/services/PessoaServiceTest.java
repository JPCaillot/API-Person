package com.project.backend.services;

import com.project.backend.domain.dtos.EnderecoDTO;
import com.project.backend.domain.dtos.PessoaDTO;
import com.project.backend.infrastructure.entities.Pessoa;
import com.project.backend.infrastructure.repositories.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {
    @Mock
    private PessoaRepository pessoaRepository;
    @InjectMocks
    private PessoaService pessoaService;
    private PessoaDTO pessoaDTO;
    @Mock
    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoaDTO = new PessoaDTO(
                null,
                "Joaquim Vieira",
                "12345678901",
                LocalDate.of(2000, 1, 1),
                new EnderecoDTO(
                     85010230,
                     "Rua 1",
                     1,
                     "Sao Paulo",
                     "SP"
                )
        );
    }

    @Test
    void givenNullIdPessoa_whenCreatePessoaWithUniqueCPFAndValidName_thenSuccessfullyCreatesPessoa() {
        when(pessoaRepository.existsByCpfAndDifferentId(anyString(), any())).thenReturn(false);

        PessoaDTO result = pessoaService.createPessoa(pessoaDTO);

        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
        assertAll(
                () -> assertEquals(pessoaDTO.getNome(), result.getNome()),
                () -> assertEquals(pessoaDTO.getCpf(), result.getCpf()),
                () -> assertEquals(pessoaDTO.getNascimento(), result.getNascimento()),
                () -> assertEquals(pessoaDTO.getEndereco().getCep(), result.getEndereco().getCep()),
                () -> assertEquals(pessoaDTO.getEndereco().getRua(), result.getEndereco().getRua()),
                () -> assertEquals(pessoaDTO.getEndereco().getNumero(), result.getEndereco().getNumero()),
                () -> assertEquals(pessoaDTO.getEndereco().getCidade(), result.getEndereco().getCidade())
        );
    }

    @Test
    void givenNullIdPessoa_whenCreatePessoaWithNonUniqueCPF_thenThrowsIllegalArgumentException() {
        when(pessoaRepository.existsByCpfAndDifferentId(anyString(), any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> pessoaService.createPessoa(pessoaDTO));
    }

    @Test
    void givenNonNullIdPessoa_whenCreatePessoa_thenThrowsIllegalArgumentException() {
        pessoaDTO.setIdPessoa(1);
        assertThrows(IllegalArgumentException.class, () -> pessoaService.createPessoa(pessoaDTO));
    }

    @Test
    void givenNonNullIdPessoa_whenUpdatePessoa_thenSuccessfullyUpdatesPessoa() {
        pessoaDTO.setIdPessoa(1);
        when(pessoaRepository.findById(pessoaDTO.getIdPessoa())).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.existsByCpfAndDifferentId(pessoaDTO.getCpf(), pessoaDTO.getIdPessoa()))
                .thenReturn(false);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO result = pessoaService.updatePessoa(pessoaDTO);

        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
        assertAll(
                () -> assertEquals(pessoaDTO.getIdPessoa(), result.getIdPessoa()),
                () -> assertEquals(pessoaDTO.getNome(), result.getNome()),
                () -> assertEquals(pessoaDTO.getCpf(), result.getCpf()),
                () -> assertEquals(pessoaDTO.getNascimento(), result.getNascimento()),
                () -> assertEquals(pessoaDTO.getEndereco().getCep(), result.getEndereco().getCep()),
                () -> assertEquals(pessoaDTO.getEndereco().getRua(), result.getEndereco().getRua()),
                () -> assertEquals(pessoaDTO.getEndereco().getNumero(), result.getEndereco().getNumero()),
                () -> assertEquals(pessoaDTO.getEndereco().getCidade(), result.getEndereco().getCidade())
        );
    }

    @Test
    void givenNonNullIdPessoa_whenUpdatePessoaWithNonUniqueCPF_thenThrowsIllegalArgumentException() {
        pessoaDTO.setIdPessoa(1);
        when(pessoaRepository.existsByCpfAndDifferentId(pessoaDTO.getCpf(), pessoaDTO.getIdPessoa()))
                .thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> pessoaService.updatePessoa(pessoaDTO));
    }

    @Test
    void givenNonNullIdPessoa_whenUpdatePessoaWithNullIdPessoa_thenThrowsIllegalArgumentException() {
        pessoaDTO.setIdPessoa(null);
        assertThrows(IllegalArgumentException.class, () -> pessoaService.updatePessoa(pessoaDTO));
    }

    @Test
    void givenExistingRecords_whenListPessoas_thenReturnsListOfPessoas() {
        when(pessoaRepository.getAllPessoasOrderedByNomeAsc())
                .thenReturn(Collections.singletonList(pessoa));

        List<PessoaDTO> result = pessoaService.listPessoas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pessoa.getIdPessoa(), result.getFirst().getIdPessoa());
    }

    @Test
    void givenValidId_whenDeletePessoa_thenDeletesPessoa() {
        pessoaDTO.setIdPessoa(1);
        pessoaService.deletePessoa(pessoaDTO.getIdPessoa());

        verify(pessoaRepository, times(1)).deleteById(pessoaDTO.getIdPessoa());
    }

    @Test
    void givenExistingCPFs_whenGetNomeByCPF_thenCorrectlyReturnsList() {
        String cpf = "12345678901";
        String nome = "John Doe";
        List<String> cpfs = Collections.singletonList("12345678901");
        when(pessoaRepository.findCpfAndNomeByCpfs(cpfs))
                .thenReturn(Collections.singletonList(new Object[] {cpf, nome}));

        Map<String, String> result = pessoaService.getNomeByCPF(cpfs);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(nome, result.get(cpf));
    }

    @Test
    void givenNotFoundCPFs_whenGetNomeByCPF_thenReturnsEmptyMap() {
        List<String> cpfs = Collections.singletonList("12345678901");
        when(pessoaRepository.findCpfAndNomeByCpfs(cpfs)).thenReturn(Collections.emptyList());

        Map<String, String> result = pessoaService.getNomeByCPF(cpfs);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
