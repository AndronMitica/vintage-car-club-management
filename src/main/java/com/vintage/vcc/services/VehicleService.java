package com.vintage.vcc.services;

import com.vintage.vcc.model.dtos.VehicleDTO;
import com.vintage.vcc.model.entities.Vehicle;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VehicleService {


    VehicleDTO createVehicle(VehicleDTO vehicleDTO);

    List <VehicleDTO> getAllVehicles();

    VehicleDTO getVehicleByLicensePlate(String licensePlate);

    List<VehicleDTO> getVehiclesByParams(String licensePlate, String make, String model, Integer year);

    VehicleDTO deleteVehicleByLicensePlate(String licensePlate);
}