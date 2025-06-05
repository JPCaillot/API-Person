package com.project.apiperson.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {
    @Id
    private Integer personId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "personId")
    private Person person;
    private Integer postalCode;
    private String street;
    private Integer number;
    private String city;
    private String state;
}
