package com.vintage.vcc.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VehicleDTO implements Serializable {

    private List<MemberDTO> owners;

    @NotBlank(message = "License plate field cannot be blank")
    private String licensePlate;

    @NotEmpty(message = "Make field cannot be empty")
    private String make;

    @NotEmpty(message = "Model field cannot be empty")
    private String model;

    @NotNull(message = "Year field cannot be null")
    private int year;

    public void setMember(MemberDTO memberDTO) {
    }

    public void setOwner(MemberDTO memberDTO) {
    }
}