package com.vintage.vcc.model.entities;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "gender",nullable = false)
    private char gender;

    @Column(name = "city", nullable = false)
    private String city;
}