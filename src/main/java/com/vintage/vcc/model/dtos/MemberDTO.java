package com.vintage.vcc.model.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class MemberDTO implements Serializable {

    private Long memberId;

    @NotEmpty(message = "First name field cannot be empty.")
    private String firstName;

    @NotEmpty(message = "Last name field cannot be empty")
    private String lastName;

    @NotEmpty(message = "Email filed cannot be empty")
    private String email;

    @NotNull(message = "Gender fields cannot be empty")
    private char gender;

    @NotEmpty(message = "City field cannot be empty")
    private String city;
}
