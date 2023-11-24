package com.vintage.vcc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintage.vcc.exceptions.VehicleCreateException;
import com.vintage.vcc.exceptions.VehicleNotFoundException;
import com.vintage.vcc.model.dtos.VehicleDTO;
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
    public List<VehicleDTO> getAllVehicles() {
        try {
            List<Vehicle> vehicleEntityList = vehicleRepository.findAll(Sort.by("licensePlate").ascending());
            log.info("The list of vehicle was retrived, count {}", vehicleEntityList.size());

            return vehicleEntityList.stream()
                    .map(vehicleEntity -> objectMapper.convertValue(vehicleEntity, VehicleDTO.class))
                    .toList();
        } catch (Exception ex) {
            log.error("Failed to retrieve all vehicles: {}", ex.getMessage());
            throw new VehicleNotFoundException("Failed to retrieve vehicles: " + ex.getMessage());
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
}