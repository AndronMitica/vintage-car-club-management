package com.vintage.vcc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintage.vcc.controlers.MemberController;
import com.vintage.vcc.exceptions.MemberNotFoundException;
import com.vintage.vcc.model.dtos.MemberDTO;
import com.vintage.vcc.model.dtos.VehicleDTO;
import com.vintage.vcc.services.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MemberControllerIntegrationTest {

    List<VehicleDTO> vehicleDTOList = List.of(
            createVehicleDTO("ABC123", "Toyota", "Camry", 2020),
            createVehicleDTO("XYZ789", "Honda", "Accord", 2019)
    );
    MemberDTO memberDTO = createMemberDTO(1L, "Fabio", "Lopez"
            , "fabio@example.com", 'M', "Paris", vehicleDTOList);

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @InjectMocks
    private MemberController memberController;

    @Test
    void createMember_ValidInput_Success() throws Exception {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(1L);
        memberDTO.setFirstName("Fabio");
        memberDTO.setLastName("Lopez");
        memberDTO.setEmail("lopez.fabio@example.com");
        memberDTO.setGender('M');
        memberDTO.setCity("Paris");
        memberDTO.setVehicles(Collections.emptyList());
        when(memberService.createMember(any(MemberDTO.class))).thenReturn(memberDTO);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Fabio"))
                .andExpect(jsonPath("$.lastName").value("Lopez"))
                .andExpect(jsonPath("$.email").value("lopez.fabio@example.com"))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.city").value("Paris"))
                .andExpect(jsonPath("$.vehicles").isEmpty());
    }
    @Test
    void createMember_InvalidInput_Failure() throws Exception {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(1L);
        memberDTO.setFirstName("Fabio");
        memberDTO.setLastName("Lopez");
        memberDTO.setEmail("lopez.fabio@example.com");
        memberDTO.setGender('M');
        memberDTO.setCity("Paris");
        memberDTO.setVehicles(Collections.emptyList());

        memberDTO.setEmail("lope.fabio@example.com");

        when(memberService.createMember(any(MemberDTO.class))).thenReturn(memberDTO);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Fabio"))
                .andExpect(jsonPath("$.lastName").value("Lopez"))
                .andExpect(jsonPath("$.email").value("lopez.fabio@example.com"))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.city").value("Paris"))
                .andExpect(jsonPath("$.vehicles").isEmpty());
    }
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void getAllMembers_Success() throws Exception {
        List<MemberDTO> memberDTOList = List.of(
                createMemberDTO(1L, "Fabio", "Lopez", "fabio@example.com", 'M', "Paris", Collections.emptyList()),
                createMemberDTO(2L, "Alice", "Smith", "alice@example.com", 'F', "New York", Collections.emptyList())
        );
        when(memberService.getAllMembers()).thenReturn(memberDTOList);

        mockMvc.perform(get("/api/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Fabio"))
                .andExpect(jsonPath("$[1].firstName").value("Alice"));
    }
    @Test
    void getAllMembers_Failure() throws Exception {
        when(memberService.getAllMembers())
                .thenThrow(new MemberNotFoundException("Failed to retrieve members"));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed to retrieve members"));
    }
    @Test
    void getMemberById_Success() throws Exception {
        Long memberId = 1L;
        MemberDTO memberDTO = createMemberDTO(memberId, "Fabio", "Lopez", "fabio@example.com", 'M', "Paris", Collections.emptyList());
        when(memberService.getMemberById(memberId)).thenReturn(memberDTO);

        mockMvc.perform(get("/api/members/{id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Fabio"))
                .andExpect(jsonPath("$.email").value("fabio@example.com"));
    }
    @Test
    void getMemberById_MemberNotFound_Failure() throws Exception {
        Long memberId = 100L;
        when(memberService.getMemberById(memberId)).thenThrow(new MemberNotFoundException("Member not found"));

        mockMvc.perform(put("/api/members/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDTO)))
                .andExpect(status().isNotFound());
    }
    @Test
    void updateMemberById_ValidInput_Success() throws Exception {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(1L);
        memberDTO.setFirstName("John");
        memberDTO.setLastName("Doe");
        memberDTO.setEmail("john@example.com");
        memberDTO.setCity("New York");

        when(memberService.updateMemberById(any(Long.class), any(MemberDTO.class))).thenReturn(memberDTO);

        mockMvc.perform(put("/api/members/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }
    @Test
    void updateMemberById_MemberNotFound() throws Exception {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(1L);
        memberDTO.setFirstName("UpdatedName");

        when(memberService.updateMemberById(any(Long.class), any(MemberDTO.class)))
                .thenThrow(new MemberNotFoundException("Member not found"));

        mockMvc.perform(put("/api/members/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Member not found"));
    }
    @Test
    void deleteMemberById_Success() throws Exception {
        MemberDTO deletedMember = new MemberDTO();
        deletedMember.setMemberId(1L);

        when(memberService.deleteMemberById(any(Long.class))).thenReturn(deletedMember);

        mockMvc.perform(delete("/api/members/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1L));
    }
    @Test
    void deleteMemberById_MemberNotFound() throws Exception {
        when(memberService.deleteMemberById(any(Long.class)))
                .thenThrow(new MemberNotFoundException("Member not found"));

        mockMvc.perform(delete("/api/members/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Member not found"));
    }
    private MemberDTO createMemberDTO(Long memberId, String firstName, String lastName, String email,
                                      char gender, String city, List<VehicleDTO> vehicles) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberId);
        memberDTO.setFirstName(firstName);
        memberDTO.setLastName(lastName);
        memberDTO.setEmail(email);
        memberDTO.setGender(gender);
        memberDTO.setCity(city);
        memberDTO.setVehicles(vehicles);
        return memberDTO;
    }
    private VehicleDTO createVehicleDTO(String licensePlate, String make, String model, int year) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setLicensePlate(licensePlate);
        vehicleDTO.setMake(make);
        vehicleDTO.setModel(model);
        vehicleDTO.setYear(year);
        return vehicleDTO;
    }
}