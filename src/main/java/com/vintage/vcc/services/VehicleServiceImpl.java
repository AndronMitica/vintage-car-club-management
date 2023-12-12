package com.vintage.vcc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintage.vcc.exceptions.VehicleCreateException;
import com.vintage.vcc.exceptions.VehicleNotFoundException;
import com.vintage.vcc.model.dtos.MemberDTO;
import com.vintage.vcc.model.dtos.VehicleDTO;
import com.vintage.vcc.model.entities.Member;
import com.vintage.vcc.model.entities.Vehicle;
import com.vintage.vcc.repositories.MemberRepository;
import com.vintage.vcc.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, MemberRepository memberRepository, ObjectMapper objectMapper) {
        this.vehicleRepository = vehicleRepository;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        String licensePlate = vehicleDTO.getLicensePlate();

        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            throw new VehicleCreateException("Vehicle with license plate '" + licensePlate + "' already exists");
        }
        return Optional.of(objectMapper.convertValue(vehicleDTO, Vehicle.class))
                .map(vehicleRepository::save)
                .map(vehicleResponseEntity -> {
                    log.info("Vehicle with license plate: {} ", vehicleResponseEntity.getLicensePlate());
                    return objectMapper.convertValue(vehicleResponseEntity, VehicleDTO.class);
                })
                .orElseThrow(() -> new VehicleCreateException("Failed to create vehicle"));
    }
    @Override
    public VehicleDTO updateVehicleByLicensePlate(String licensePlate, VehicleDTO vehicleDTO) {
        return Optional.ofNullable(vehicleRepository.findByLicensePlate(licensePlate))
                .map(existingVehicle -> {
                    existingVehicle.setMake(vehicleDTO.getMake());
                    existingVehicle.setModel(vehicleDTO.getModel());
                    existingVehicle.setYear(vehicleDTO.getYear());

                    Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);

                    log.info("Vehicle with license plate: {} was updated", licensePlate);
                    return objectMapper.convertValue(updatedVehicle, VehicleDTO.class);
                })
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with license plate: " + licensePlate + " not found"));
    }
    @Transactional
    @Override
    public VehicleDTO deleteVehicleByLicensePlate(String licensePlate) {
        return Optional.ofNullable(vehicleRepository.findByLicensePlate(licensePlate))
                .map(vehicle -> {
                    VehicleDTO vehicleDTO = objectMapper.convertValue(vehicle, VehicleDTO.class);
                    vehicleRepository.deleteByLicensePlate(licensePlate);
                    log.info("Vehicle with license plate: {} was deleted", licensePlate);
                    return vehicleDTO;
                })
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with license plate: " + licensePlate + " not found"));
    }
    @Override
    public VehicleDTO getVehicleByLicensePlate(String licensePlate) throws VehicleNotFoundException {
        return Optional.ofNullable(vehicleRepository.findByLicensePlate(licensePlate))
                .map(vehicleEntity -> objectMapper.convertValue(vehicleEntity, VehicleDTO.class))
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with license plate: " + licensePlate + " not found"));
    }


    @Override
    public List<VehicleDTO> getVehiclesByParams(String licensePlate, String make, String model, Integer year) {
        return Optional.of(vehicleRepository.findVehiclesByParams(licensePlate, make, model, year))
                .map(vehicleEntities -> vehicleEntities.stream()
                        .map(vehicle -> objectMapper.convertValue(vehicle, VehicleDTO.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new VehicleNotFoundException("Failed to retrieve vehicles"));
    }
    @Override
    public List<VehicleDTO> getAllVehicles() {
        return Optional.of(vehicleRepository.findAll(Sort.by("licensePlate").ascending()))
                .map(vehicleEntityList -> vehicleEntityList.stream()
                        .map(this::mapVehicleToDTOWithMembers)
                        .toList())
                .orElseThrow(() -> new VehicleNotFoundException("Failed to retrieve vehicles"));
    }
    private VehicleDTO mapVehicleToDTOWithMembers(Vehicle vehicle) {
        VehicleDTO vehicleDTO = mapVehicleToDTO(vehicle);

        List<MemberDTO> memberDTOs = mapMembersToDTOs(vehicle.getOwners());
        vehicleDTO.setOwners(memberDTOs);
        return vehicleDTO;
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

    private List<MemberDTO> mapMembersToDTOs(List<Member> members) {
        return members.stream()
                .map(this::mapMemberToDTO)
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