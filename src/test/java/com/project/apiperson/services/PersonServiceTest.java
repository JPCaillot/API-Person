package com.project.apiperson.services;

import com.project.apiperson.domain.dtos.AddressDTO;
import com.project.apiperson.domain.dtos.PersonDTO;
import com.project.apiperson.infrastructure.entities.Person;
import com.project.apiperson.infrastructure.repositories.PersonRepository;
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
class PersonServiceTest {
    @Mock
    private PersonRepository repository;
    @InjectMocks
    private PersonService personService;
    private PersonDTO personDTO;
    @Mock
    private Person person;

    @BeforeEach
    void setUp() {
        personDTO = new PersonDTO(
                null,
                "Joaquim Vieira",
                "12345678901",
                LocalDate.of(2000, 1, 1),
                new AddressDTO(
                     85010230,
                     "Rua 1",
                     1,
                     "Sao Paulo",
                     "SP"
                )
        );
    }

    @Test
    void givenNullPersonId_whenCreatePersonWithUniqueDocumentNumberAndValidName_thenSuccessfullyCreatesPessoa() {
        when(repository.existsByDocumentNumberAndDifferentId(anyString(), any())).thenReturn(false);

        PersonDTO result = personService.createPerson(personDTO);

        verify(repository, times(1)).save(any(Person.class));
        assertAll(
                () -> assertEquals(personDTO.getName(), result.getName()),
                () -> assertEquals(personDTO.getDocumentNumber(), result.getDocumentNumber()),
                () -> assertEquals(personDTO.getBirthdate(), result.getBirthdate()),
                () -> assertEquals(personDTO.getAddress().getPostalCode(), result.getAddress().getPostalCode()),
                () -> assertEquals(personDTO.getAddress().getStreet(), result.getAddress().getStreet()),
                () -> assertEquals(personDTO.getAddress().getNumber(), result.getAddress().getNumber()),
                () -> assertEquals(personDTO.getAddress().getCity(), result.getAddress().getCity())
        );
    }

    @Test
    void givenNullPersonId_whenCreatePersonWithNonUniqueDocumentNumber_thenThrowsIllegalArgumentException() {
        when(repository.existsByDocumentNumberAndDifferentId(anyString(), any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> personService.createPerson(personDTO));
    }

    @Test
    void givenNonNullPersonId_whenCreatePerson_thenThrowsIllegalArgumentException() {
        personDTO.setPersonId(1);
        assertThrows(IllegalArgumentException.class, () -> personService.createPerson(personDTO));
    }

    @Test
    void givenNonNullPersonId_whenUpdatePerson_thenSuccessfullyUpdatesPerson() {
        personDTO.setPersonId(1);
        when(repository.findById(personDTO.getPersonId())).thenReturn(Optional.of(person));
        when(repository.existsByDocumentNumberAndDifferentId(personDTO.getDocumentNumber(), personDTO.getPersonId()))
                .thenReturn(false);
        when(repository.save(any(Person.class))).thenReturn(person);

        PersonDTO result = personService.updatePerson(personDTO);

        verify(repository, times(1)).save(any(Person.class));
        assertAll(
                () -> assertEquals(personDTO.getPersonId(), result.getPersonId()),
                () -> assertEquals(personDTO.getName(), result.getName()),
                () -> assertEquals(personDTO.getDocumentNumber(), result.getDocumentNumber()),
                () -> assertEquals(personDTO.getBirthdate(), result.getBirthdate()),
                () -> assertEquals(personDTO.getAddress().getPostalCode(), result.getAddress().getPostalCode()),
                () -> assertEquals(personDTO.getAddress().getStreet(), result.getAddress().getStreet()),
                () -> assertEquals(personDTO.getAddress().getNumber(), result.getAddress().getNumber()),
                () -> assertEquals(personDTO.getAddress().getCity(), result.getAddress().getCity())
        );
    }

    @Test
    void givenNonNullPersonId_whenUpdatePersonWithNonUniqueDocumentNumber_thenThrowsIllegalArgumentException() {
        personDTO.setPersonId(1);
        when(repository.existsByDocumentNumberAndDifferentId(personDTO.getDocumentNumber(), personDTO.getPersonId()))
                .thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> personService.updatePerson(personDTO));
    }

    @Test
    void givenNonNullPersonId_whenUpdatePersonWithNullPersonId_thenThrowsIllegalArgumentException() {
        personDTO.setPersonId(null);
        assertThrows(IllegalArgumentException.class, () -> personService.updatePerson(personDTO));
    }

    @Test
    void givenExistingRecords_whenListPersons_thenReturnsListOfPersons() {
        when(repository.getAllPersonsOrderedByNameAsc()).thenReturn(Collections.singletonList(person));

        List<PersonDTO> result = personService.listPersons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(person.getPersonId(), result.getFirst().getPersonId());
    }

    @Test
    void givenValidId_whenDeletePerson_thenDeletesPerson() {
        personDTO.setPersonId(1);
        personService.deletePerson(personDTO.getPersonId());

        verify(repository, times(1)).deleteById(personDTO.getPersonId());
    }

    @Test
    void givenExistingDocumentNumbers_whenGetNameByDocumentNumber_thenCorrectlyReturnsList() {
        String document = "12345678901";
        String name = "John Doe";
        List<String> documents = Collections.singletonList("12345678901");
        when(repository.findDocumentNumberAndNameByDocumentNumbers(documents))
                .thenReturn(Collections.singletonList(new Object[] {document, name}));

        Map<String, String> result = personService.getNameByDocumentNumber(documents);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(name, result.get(document));
    }

    @Test
    void givenNotFoundDocumentNumbers_whenGetNameByDocumentNumber_thenReturnsEmptyMap() {
        List<String> documents = Collections.singletonList("12345678901");
        when(repository.findDocumentNumberAndNameByDocumentNumbers(documents)).thenReturn(Collections.emptyList());

        Map<String, String> result = personService.getNameByDocumentNumber(documents);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
