package com.blindworks.rhenanenmanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String addon;

    private String zip;

    private String city;

    private String state;

    private String country;

    private Double latitude;

    private Double longitude;

    private String type;
}
