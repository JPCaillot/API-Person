package com.project.apiperson.services;

import com.project.apiperson.domain.dtos.PersonDTO;
import com.project.apiperson.domain.validators.PersonValidator;
import com.project.apiperson.infrastructure.entities.Address;
import com.project.apiperson.infrastructure.entities.Person;
import com.project.apiperson.infrastructure.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PersonService implements IPersonService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public PersonService(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public PersonDTO createPerson(PersonDTO personDTO) {
        if (Objects.isNull(personDTO.getPersonId())) {
            String nome = personDTO.getName();
            PersonValidator.validateName(nome);
            validateDocumentNumberUniqueness(personDTO);

            log.info("Creating Person record with name: {}", personDTO.getName());
            Person person = new Person();

            modelMapper.map(personDTO, person);
            assignAddress(person);
            personRepository.save(person);
            log.info("Record creation for {} is complete", person.getName());

            modelMapper.map(person, personDTO);
        } else {
            throw new IllegalArgumentException("personId must be null for a new record creation");
        }

        return personDTO;
    }

    @Override
    @Transactional
    public PersonDTO updatePerson(PersonDTO personDTO) {
        String nome = personDTO.getName();
        PersonValidator.validateName(nome);
        validateDocumentNumberUniqueness(personDTO);

        Person person = retrievePerson(personDTO);
        modelMapper.map(personDTO, person);
        assignAddress(person);

        personRepository.save(person);
        log.info("Record update for {} is complete", person.getName());
        modelMapper.map(person, personDTO);
        return personDTO;
    }

    @Override
    public List<PersonDTO> listPersons() {
        List<PersonDTO> response = new ArrayList<>();

        personRepository.getAllPersonsOrderedByNameAsc().
                forEach(pessoa -> {
                    log.info("Mapping record of {}", pessoa.getName());
                    PersonDTO personDTO = new PersonDTO();
                    modelMapper.map(pessoa, personDTO);
                    response.add(personDTO);
                });
        log.info("{} records found", response.size());
        return response;
    }

    @Override
    @Transactional
    public void deletePerson(Integer personId) {
        personRepository.deleteById(personId);
        log.info("Record of id {} successfully deleted", personId);
    }

    @Override
    public Map<String, String> getNameByDocumentNumber(List<String> documentNumbers) {
        List<Object[]> results = personRepository.findDocumentNumberAndNameByDocumentNumbers(documentNumbers);
        Map<String, String> nomeOfEachCPF = new HashMap<>();

        for (Object[] result : results) {
            String cpf = (String) result[0];
            String nome = (String) result[1];
            nomeOfEachCPF.put(cpf, nome);
        }

        return nomeOfEachCPF;
    }

    private void validateDocumentNumberUniqueness(PersonDTO personDTO) {
        String nome = personDTO.getName();
        log.info("Validating document number from {}", nome);
        if (personRepository.existsByDocumentNumberAndDifferentId(personDTO.getDocumentNumber(), personDTO.getPersonId())) {
            throw new IllegalArgumentException("Document number already present in another record");
        }
        log.info("Data validation of {} is complete", nome);
    }

    private Person retrievePerson(PersonDTO personDTO) {
        Person person;
        if (Objects.nonNull(personDTO.getPersonId())) {
            log.info("Updating record of {}", personDTO.getName());
            person = personRepository.findById(personDTO.getPersonId())
                    .orElseThrow(() -> new ObjectNotFoundException(Person.class.getName(), personDTO.getPersonId()));
            log.info("Record found, updating...");
        } else {
            throw new IllegalArgumentException("personId can't be null for record update");
        }
        return person;
    }

    private static void assignAddress(Person person) {
        Address address = person.getAddress();
        if (Objects.nonNull(address)) {
            address.setPerson(person);
        }
    }
}
