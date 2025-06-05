package com.project.apiperson.controllers;

import com.project.apiperson.domain.dtos.PersonDTO;
import com.project.apiperson.services.IPersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/person")
@Validated
public class PersonController {
    private final IPersonService personService;

    public PersonController(final IPersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody @NotNull PersonDTO personDTO) {
        PersonDTO person = personService.createPerson(personDTO);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<PersonDTO> updatePerson(@Valid @RequestBody @NotNull PersonDTO personDTO) {
        PersonDTO person = personService.updatePerson(personDTO);
        return new ResponseEntity<>(person, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<PersonDTO>> listPersons() {
        List<PersonDTO> persons = personService.listPersons();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @DeleteMapping("/{personId}")
    public void deletePerson(@PathVariable @NotNull Integer personId) {
        personService.deletePerson(personId);
    }

    @GetMapping("/cpfs")
    public ResponseEntity<Map<String, String>> getNameByCPF(@Valid @RequestParam List<String> cpfs) {
        Map<String, String> personName = personService.getNameByDocumentNumber(cpfs);
        return new ResponseEntity<>(personName, HttpStatus.OK);
    }
}
