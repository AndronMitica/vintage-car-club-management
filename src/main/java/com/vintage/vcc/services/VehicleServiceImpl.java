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
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) throws VehicleCreateException {
        try {
        Vehicle vehicleEntity = objectMapper.convertValue(vehicleDTO, Vehicle.class);

        Vehicle vehicleResponseEntity = vehicleRepository.save(vehicleEntity);
        log.info("Vehicle with license plate: {} ", vehicleResponseEntity.getLicensePlate());

        return objectMapper.convertValue(vehicleResponseEntity, VehicleDTO.class);
    } catch (Exception ex) {
            log.error("Failed to create member: {}", ex.getMessage());
            throw new VehicleCreateException("Failed to create vehicle: " + ex.getMessage());
        }
    }

    @Override
    public VehicleDTO updateVehicleByLicensePlate(String licensePlate, VehicleDTO vehicleDTO) {
        try {
            Optional<Vehicle> vehicleOptional = Optional.ofNullable(vehicleRepository.findByLicensePlate(licensePlate));

            if (vehicleOptional.isPresent()) {
                Vehicle existingVehicle = vehicleOptional.get();

                existingVehicle.setMake(vehicleDTO.getMake());
                existingVehicle.setModel(vehicleDTO.getModel());
                existingVehicle.setYear(vehicleDTO.getYear());

                Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);

                log.info("Vehicle with license plate: {} was updated ", licensePlate);
                return objectMapper.convertValue(updatedVehicle, VehicleDTO.class);
            }

            log.info("Vehicle with license plate: {} was not found", licensePlate);
            return null;
        } catch (Exception ex) {
            log.error("Failed to update vehicle: {}", ex.getMessage());
            throw new VehicleNotFoundException("Failed to update vehicle: " + ex.getMessage());
        }
    }


    @Transactional
    @Override
    public VehicleDTO deleteVehicleByLicensePlate(String licensePlate) throws VehicleNotFoundException {
        try {
            Optional<Vehicle> vehicleOptional = Optional.ofNullable(vehicleRepository.findByLicensePlate(licensePlate));

            if (vehicleOptional.isPresent()) {
                VehicleDTO vehicleDTO = objectMapper.convertValue(vehicleOptional, VehicleDTO.class);
                vehicleRepository.deleteByLicensePlate(licensePlate);
                log.info("Vehicle with license plate: {} was deleted ", licensePlate);
                return vehicleDTO;
            }
            log.info("Vehicle with license plate: {} was not found", licensePlate);
            return null;
        } catch (Exception ex) {
            log.error("Failed to delete vehicle: {}", ex.getMessage());
            throw new VehicleNotFoundException("Failed to delete vehicle: " + ex.getMessage());
        }
    }


    @Override
    public VehicleDTO getVehicleByLicensePlate(String licensePlate) throws VehicleNotFoundException {
        try {
            Optional<Vehicle> vehicleOptional = Optional.ofNullable(vehicleRepository.findByLicensePlate(licensePlate));

            if (vehicleOptional.isPresent()) {
                Vehicle vehicleEntity = vehicleOptional.get();
                return objectMapper.convertValue(vehicleEntity, VehicleDTO.class);
            }
            log.info("Vehicle with license plate: {} was not found", licensePlate);
            throw new VehicleNotFoundException("Vehicle with license plate: " + licensePlate + " not found");
        } catch (Exception ex) {
            throw new VehicleNotFoundException("Failed to retrieve member: " + ex.getMessage());
        }
    }

    @Override
    public List<VehicleDTO> getVehiclesByParams(String licensePlate, String make, String model, Integer year) {
        try {

            List<Vehicle> vehicleEntities = vehicleRepository.findVehiclesByParams(licensePlate, make, model, year);
            return vehicleEntities.stream()
                    .map(vehicle -> objectMapper.convertValue(vehicle, VehicleDTO.class))
                    .collect(Collectors.toList());
        }catch (Exception ex) {
            log.error("Failed to retrieve vehicle: {}", ex.getMessage());
            throw new VehicleNotFoundException("Failed to retrieve vehicle: " + ex.getMessage());
        }
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        try {
            List<Vehicle> vehicleEntityList = vehicleRepository.findAll(Sort.by("licensePlate").ascending());
            log.info("The list of vehicles was retrieved, count {}", vehicleEntityList.size());

            return vehicleEntityList.stream()
                    .map(this::mapVehicleToDTOWithMembers)
                    .toList();
        } catch (Exception ex) {
            log.error("Failed to retrieve all vehicles: {}", ex.getMessage());
            throw new VehicleNotFoundException("Failed to retrieve vehicles: " + ex.getMessage());
        }
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