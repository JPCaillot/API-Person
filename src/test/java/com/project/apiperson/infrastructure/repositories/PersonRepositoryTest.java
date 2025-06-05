package com.project.apiperson.infrastructure.repositories;

import com.project.apiperson.infrastructure.entities.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;
    private Person person1;
    private Person person2;
    private static final String DOCUMENT_1 = "10987654321";
    private static final String DOCUMENT_2 = "12345678901";

    @BeforeEach
    void setUp() {
        person1 = new Person(
                null,
                "Ana Oliveira",
                DOCUMENT_1,
                LocalDate.of(2000, 1, 1),
                null
        );
        person2 = new Person(
                null,
                "Joaquim Vieira",
                DOCUMENT_2,
                LocalDate.of(2000, 1, 1),
                null
        );
        personRepository.saveAll(List.of(person1, person2));
    }

    @Test
    void givenExistingRecords_whenGetAllPersonsOrderedByNameAsc_thenReturnsOrderedListOfPersons() {
        List<Person> result = personRepository.getAllPersonsOrderedByNameAsc();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ana Oliveira", result.get(0).getName());
        assertEquals("Joaquim Vieira", result.get(1).getName());
    }

    @Test
    void givenNonExistingRecords_whenGetAllPersonsOrderedByNameAsc_thenReturnsEmptyList() {
        personRepository.deleteAll();

        List<Person> result = personRepository.getAllPersonsOrderedByNameAsc();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenVerificationOfExistingRecord_whenExistsByDocumentNumberAndDifferentId_thenReturnsFalse() {
        boolean result = personRepository.existsByDocumentNumberAndDifferentId(DOCUMENT_1, person1.getPersonId());

        assertFalse(result);
    }

    @Test
    void givenVerificationOfNewUniqueCPFValue_whenExistsByDocumentNumberAndDifferentId_thenReturnsFalse() {
        boolean result = personRepository.existsByDocumentNumberAndDifferentId("55555555555", null);

        assertFalse(result);
    }

    @Test
    void givenVerificationOfNonUniqueCpf_whenExistsByDocumentNumberAndDifferentId_thenReturnsTrue() {
        boolean result = personRepository.existsByDocumentNumberAndDifferentId(DOCUMENT_1, person2.getPersonId());

        assertTrue(result);
    }

    @Test
    void givenRequestOfExistingRecords_whenFindDocumentNumberAndNameByDocumentNumbers_thenReturnsListOfNamesWithCpfs() {
        List<Object[]> result = personRepository
                .findDocumentNumberAndNameByDocumentNumbers(List.of(DOCUMENT_1, DOCUMENT_2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(person1.getDocumentNumber(), result.get(0)[0]);
        assertEquals(person1.getName(), result.get(0)[1]);
        assertEquals(person2.getDocumentNumber(), result.get(1)[0]);
        assertEquals(person2.getName(), result.get(1)[1]);
    }

    @Test
    void givenRequestOfSomeExistingRecords_whenFindDocumentNumberAndNameByDocumentNumbers_thenReturnsPartialListOfNamesWithCpf() {
        List<Object[]> result = personRepository
                .findDocumentNumberAndNameByDocumentNumbers(List.of(DOCUMENT_1, "99999999999"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(person1.getDocumentNumber(), result.getFirst()[0]);
        assertEquals(person1.getName(), result.getFirst()[1]);
    }

    @Test
    void givenRequestOfNonExistingRecords_whenFindDocumentNumberAndNameByDocumentNumbers_thenReturnsEmptyList() {
        List<Object[]> result = personRepository
                .findDocumentNumberAndNameByDocumentNumbers(List.of("99999999999"));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }
}
