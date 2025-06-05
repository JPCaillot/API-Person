package com.project.apiperson.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private Integer personId;
    @NotBlank(message = "The name can't be empty")
    private String name;
    @NotBlank(message = "CPF is a required field")
    @Length(min = 11, max = 11)
    private String documentNumber;
    @NotNull
    @Past(message = "Birthdate can't be greater than the current one")
    private LocalDate birthdate;
    @Valid
    private AddressDTO address;
}
