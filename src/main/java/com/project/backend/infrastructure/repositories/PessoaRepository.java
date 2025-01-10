package com.project.backend.infrastructure.repositories;

import com.project.backend.infrastructure.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
    List<Pessoa> findByIdPessoaNotNullOrderByNomeAsc();

    @Query(
            value = "SELECT EXISTS (" +
            "    SELECT 1 " +
            "    FROM pessoa p " +
            "    WHERE p.cpf = :cpf AND (p.id_pessoa <> :idPessoa OR :idPessoa is null)" +
            ")",
            nativeQuery = true
    )
    boolean existsByCpfAndDifferentId(String cpf, Integer idPessoa);

    @Query(value = "SELECT cpf, nome FROM pessoa WHERE cpf IN (:cpfs)", nativeQuery = true)
    List<Object[]> findCpfAndNomeByCpfs(List<String> cpfs);
}
