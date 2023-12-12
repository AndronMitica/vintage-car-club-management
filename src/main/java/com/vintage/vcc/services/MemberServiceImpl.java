package com.vintage.vcc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    @Autowired
    private final VehicleRepository vehicleRepository;

    private final ObjectMapper objectMapper;
    private final VehicleService vehicleService;

    public MemberServiceImpl(MemberRepository memberRepository,
                             VehicleRepository vehicleRepository,
                             ObjectMapper objectMapper,
                             VehicleService vehicleService) {
        this.memberRepository = memberRepository;
        this.vehicleRepository = vehicleRepository;
        this.objectMapper = objectMapper;
        this.vehicleService = vehicleService;
    }

    @Override
    public MemberDTO createMember(MemberDTO memberDTO) {
        Member memberEntity = objectMapper.convertValue(memberDTO, Member.class);
        Member memberResponseEntity = memberRepository.save(memberEntity);
        log.info("Member with id: {} was created", memberResponseEntity.getMemberId());

        return objectMapper.convertValue(memberResponseEntity, MemberDTO.class);
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        return memberRepository.findById(id)
                .map(memberEntity -> objectMapper.convertValue(memberEntity, MemberDTO.class))
                .orElseThrow(() -> new MemberNotFoundException("Member with id: " + id + " not found"));
    }

    @Override
    public MemberDTO updateMemberById(Long id, MemberDTO memberDTO) {
        return memberRepository.findById(id)
                .map(existingMember -> {
                    memberDTO.setMemberId(id);
                    Member updatedMemberEntity = objectMapper.convertValue(memberDTO, Member.class);
                    Member memberResponseEntity = memberRepository.save(updatedMemberEntity);
                    log.info("Member with id: {} was updated", memberResponseEntity.getMemberId());
                    return objectMapper.convertValue(memberResponseEntity, MemberDTO.class);
                })
                .orElseThrow(() -> new MemberNotFoundException("Member with id: " + id + " not found"));
    }

    @Override
    public MemberDTO deleteMemberById(Long id) {
        return memberRepository.findById(id)
                .map(memberEntity -> {
                    MemberDTO memberDTO = objectMapper.convertValue(memberEntity, MemberDTO.class);
                    memberRepository.deleteById(id);
                    log.info("Member with id: {} was deleted", id);
                    return memberDTO;
                })
                .orElseThrow(() -> new MemberNotFoundException("Member with id: " + id + " not found"));
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
        return memberRepository.findAll(Sort.by("memberId"))
                .stream()
                .map(this::mapMemberToDTOWithVehicles)
                .toList();
    }

    private MemberDTO mapMemberToDTOWithVehicles(Member member) {
        MemberDTO memberDTO = mapMemberToDTO(member);

        List<VehicleDTO> vehicleDTOs = mapVehiclesToDTOs(member.getVehicles());
        memberDTO.setVehicles(vehicleDTOs);
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