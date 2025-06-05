package com.project.apiperson.infrastructure.repositories;

import com.project.apiperson.infrastructure.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query(
            value = "select p " +
                    "from Person p " +
                    "order by p.name")
    List<Person> getAllPersonsOrderedByNameAsc();

    @Query(
            value = "SELECT EXISTS (" +
            "    SELECT 1 " +
            "    FROM Person p " +
            "    WHERE p.documentNumber = :documentNumber AND (p.personId <> :personId OR :personId is null)" +
            ")"
    )
    boolean existsByDocumentNumberAndDifferentId(String documentNumber, Integer personId);

    @Query(value = "SELECT p.documentNumber, p.name FROM Person p WHERE p.documentNumber IN (:documentNumbers)")
    List<Object[]> findDocumentNumberAndNameByDocumentNumbers(List<String> documentNumbers);
}
