package com.vintage.vcc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintage.vcc.exceptions.MemberCreateException;
import com.vintage.vcc.exceptions.MemberNotFoundException;
import com.vintage.vcc.model.dtos.MemberDTO;
import com.vintage.vcc.model.dtos.VehicleDTO;
import com.vintage.vcc.model.entities.Member;
import com.vintage.vcc.model.entities.Vehicle;
import com.vintage.vcc.repositories.MemberRepository;
import com.vintage.vcc.repositories.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    @Autowired
    private final VehicleRepository vehicleRepository;

    private final ObjectMapper objectMapper;
    private final VehicleService vehicleService;

    public MemberServiceImpl(MemberRepository memberRepository
            , VehicleRepository vehicleRepository
            , ObjectMapper objectMapper
            , VehicleService vehicleService) {
        this.memberRepository = memberRepository;
        this.vehicleRepository = vehicleRepository;
        this.objectMapper = objectMapper;
        this.vehicleService = vehicleService;
    }

    @Override
    public MemberDTO createMember(MemberDTO memberDTO) throws MemberCreateException {
        try {
            Member memberEntity = objectMapper.convertValue(memberDTO, Member.class);

            Member memberResponseEntity = memberRepository.save(memberEntity);
            log.info("Member with id: {} was created", memberResponseEntity.getMemberId());

            return objectMapper.convertValue(memberResponseEntity, MemberDTO.class);
        } catch (Exception ex) {
            log.error("Failed to create member: {}", ex.getMessage());
            throw new MemberCreateException("Failed to create member: " + ex.getMessage());
        }
    }

    @Override
    public MemberDTO getMemberById(Long id) throws MemberNotFoundException {
        try {
            Optional<Member> optionalMember = memberRepository.findById(id);
            if (optionalMember.isPresent()) {
                Member memberEntity = optionalMember.get();
                return objectMapper.convertValue(memberEntity, MemberDTO.class);
            }
            log.info("Member with id: {} was not found", id);
            throw new MemberNotFoundException("Member with id: " + id + " not found");
        } catch (Exception ex) {
            log.error("Failed to retrieve member: {}", ex.getMessage());
            throw new MemberNotFoundException("Failed to retrieve member: " + ex.getMessage());
        }
    }

    @Override
    public MemberDTO updateMemberById(Long id, MemberDTO memberDTO) throws MemberNotFoundException {
        try {
            if (memberRepository.findById(id).isPresent()) {
                memberDTO.setMemberId(id);
                Member memberEntity = objectMapper.convertValue(memberDTO, Member.class);
                Member memberResponseEntity = memberRepository.save(memberEntity);
                log.info("Member with id: {} was updated", memberResponseEntity.getMemberId());
                return objectMapper.convertValue(memberResponseEntity, MemberDTO.class);
            }
            log.info("Member with id: {} was not found", id);
            return null;
        } catch (Exception ex) {
            log.error("Failed to update member: {}", ex.getMessage());
            throw new MemberNotFoundException("Failed to update member: " + ex.getMessage());
        }
    }

    @Override
    public MemberDTO deleteMemberById(Long id) throws MemberNotFoundException {
        try {
            Optional<Member> memberEntity = memberRepository.findById(id);

            if (memberEntity.isPresent()) {
                MemberDTO memberDTO = objectMapper.convertValue(memberEntity, MemberDTO.class);
                memberRepository.deleteById(id);
                log.info("Member with id: {} was deleted", id);
                return memberDTO;
            }
            log.info("Member with id: {} was not founded", id);
            return null;
        } catch (Exception ex) {
            log.error("Failed to delete member: {}", ex.getMessage());
            throw new MemberNotFoundException("Failed to delete member: " + ex.getMessage());
        }
    }

    @Override
    public void assignVehicleToMember(Long id, String licensePlate) {
        Member memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        Vehicle vehicleEntity = vehicleRepository.findByLicensePlate(licensePlate);

        memberEntity.getVehicles().add(vehicleEntity);
        vehicleEntity.getOwners().add(memberEntity);

        memberRepository.save(memberEntity);
        vehicleRepository.save(vehicleEntity);
    }

    public List<MemberDTO> getAllMembers() {
        try {
            List<Member> membersEntityList = memberRepository.findAll(Sort.by("memberId"));
            log.info("The list of members was retrieved, count {}", membersEntityList.size());

            return membersEntityList.stream()
                    .map(this::mapMemberToDTOWithVehicles)
                    .toList();
        } catch (Exception ex) {
            log.error("Failed to retrieve all members: {}", ex.getMessage());
            throw new MemberNotFoundException("Failed to retrieve members: " + ex.getMessage());
        }
    }

    private MemberDTO mapMemberToDTOWithVehicles(Member member) {
        MemberDTO memberDTO = mapMemberToDTO(member); // Map basic member details

        List<VehicleDTO> vehicleDTOs = mapVehiclesToDTOs(member.getVehicles()); // Map vehicles to DTOs
        memberDTO.setVehicles(vehicleDTOs); // Set the vehicles in the MemberDTO
        return memberDTO;
    }


    private MemberDTO mapMemberToDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(member.getMemberId());
        memberDTO.setFirstName(member.getFirstName());
        memberDTO.setLastName(member.getLastName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setGender(member.getGender());
        memberDTO.setCity(member.getCity());
        return memberDTO;
    }

    private List<VehicleDTO> mapVehiclesToDTOs(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(this::mapVehicleToDTO)
                .toList();
    }

    private VehicleDTO mapVehicleToDTO(Vehicle vehicle) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setLicensePlate(vehicle.getLicensePlate());
        vehicleDTO.setMake(vehicle.getMake());
        vehicleDTO.setModel(vehicle.getModel());
        vehicleDTO.setYear(vehicle.getYear());
        return vehicleDTO;
    }
}