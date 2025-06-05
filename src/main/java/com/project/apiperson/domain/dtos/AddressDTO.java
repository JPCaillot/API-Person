package com.project.apiperson.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    @NotNull(message = "CEP is a required field")
    private Integer postalCode;
    @NotBlank(message = "Street is a required field")
    private String street;
    private Integer number;
    @NotBlank(message = "City is a required field")
    private String city;
    @NotBlank(message = "State is a required field")
    private String state;
}
