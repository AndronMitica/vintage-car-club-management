package com.vintage.vcc.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @Column(name = "license_plate", unique = true)
    private String licensePlate;

    @Column(name = "make", nullable = false)
    private String make;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private int year;

    @JsonIgnore
    @ManyToMany(mappedBy = "vehicles", fetch = FetchType.EAGER)
    private List<Member> owners = new ArrayList<>();
}