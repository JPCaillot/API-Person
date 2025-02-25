package com.project.backend.infrastructure.repositories;

import com.project.backend.infrastructure.entities.Pessoa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PessoaRepositoryTest {
    @Autowired
    private PessoaRepository pessoaRepository;
    private Pessoa pessoa1;
    private Pessoa pessoa2;
    private static final String CPF_1 = "10987654321";
    private static final String CPF_2 = "12345678901";

    @BeforeEach
    void setUp() {
        pessoa1 = new Pessoa(
                null,
                "Ana Oliveira",
                CPF_1,
                LocalDate.of(2000, 1, 1),
                null
        );
        pessoa2 = new Pessoa(
                null,
                "Joaquim Vieira",
                CPF_2,
                LocalDate.of(2000, 1, 1),
                null
        );
        pessoaRepository.saveAll(List.of(pessoa1, pessoa2));
    }

    @Test
    void givenExistingRecords_whenGetAllPessoasOrderedByNomeAsc_thenReturnsOrderedListOfPessoas() {
        List<Pessoa> result = pessoaRepository.getAllPessoasOrderedByNomeAsc();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ana Oliveira", result.get(0).getNome());
        assertEquals("Joaquim Vieira", result.get(1).getNome());
    }

    @Test
    void givenNonExistingRecords_whenGetAllPessoasOrderedByNomeAsc_thenReturnsEmptyList() {
        pessoaRepository.deleteAll();

        List<Pessoa> result = pessoaRepository.getAllPessoasOrderedByNomeAsc();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenVerificationOfExistingRecord_whenExistsByCpfAndDifferentId_thenReturnsFalse() {
        boolean result = pessoaRepository.existsByCpfAndDifferentId(CPF_1, pessoa1.getIdPessoa());

        assertFalse(result);
    }

    @Test
    void givenVerificationOfNewUniqueCPFValue_whenExistsByCpfAndDifferentId_thenReturnsFalse() {
        boolean result = pessoaRepository.existsByCpfAndDifferentId("55555555555", null);

        assertFalse(result);
    }

    @Test
    void givenVerificationOfNonUniqueCpf_whenExistsByCpfAndDifferentId_thenReturnsTrue() {
        boolean result = pessoaRepository.existsByCpfAndDifferentId(CPF_1, pessoa2.getIdPessoa());

        assertTrue(result);
    }

    @Test
    void givenRequestOfExistingRecords_whenFindCpfAndNomeByCpfs_thenReturnsListOfNamesWithCpfs() {
        List<Object[]> result = pessoaRepository.findCpfAndNomeByCpfs(List.of(CPF_1, CPF_2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(pessoa1.getCpf(), result.get(0)[0]);
        assertEquals(pessoa1.getNome(), result.get(0)[1]);
        assertEquals(pessoa2.getCpf(), result.get(1)[0]);
        assertEquals(pessoa2.getNome(), result.get(1)[1]);
    }

    @Test
    void givenRequestOfSomeExistingRecords_whenFindCpfAndNomeByCpfs_thenReturnsPartialListOfNamesWithCpf() {
        List<Object[]> result = pessoaRepository.findCpfAndNomeByCpfs(List.of(CPF_1, "99999999999"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pessoa1.getCpf(), result.getFirst()[0]);
        assertEquals(pessoa1.getNome(), result.getFirst()[1]);
    }

    @Test
    void givenRequestOfNonExistingRecords_whenFindCpfAndNomeByCpfs_thenReturnsEmptyList() {
        List<Object[]> result = pessoaRepository.findCpfAndNomeByCpfs(List.of("99999999999"));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @AfterEach
    void tearDown() {
        pessoaRepository.deleteAll();
    }
}
