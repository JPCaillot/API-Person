package com.project.apiperson.services;

import com.project.apiperson.domain.dtos.PersonDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public interface IPersonService {
    PersonDTO createPerson(PersonDTO person);
    PersonDTO updatePerson(PersonDTO person);
    List<PersonDTO> listPersons();
    void deletePerson(Integer personId);
    Map<String, String> getNameByDocumentNumber(@Valid @NotBlank List<String> documentNumbers);
}
