package com.vintage.vcc.controlers;

import com.vintage.vcc.model.dtos.VehicleDTO;
import com.vintage.vcc.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/vehicles")
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicleDTO));
    }

    @GetMapping("/vehicles")
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @PutMapping("/vehicles/{licensePlate}")
    public ResponseEntity<VehicleDTO> updateVehicleByLicensePlate(
            @PathVariable @Valid String licensePlate,
            @RequestBody @Valid VehicleDTO vehicleDTO) {
        Optional<VehicleDTO> vehicleDTOResponse = Optional.ofNullable(vehicleService.updateVehicleByLicensePlate(licensePlate, vehicleDTO));
        return vehicleDTOResponse
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/vehicles/{licensePlate}")
    public ResponseEntity<VehicleDTO> deleteVehicleByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(vehicleService.deleteVehicleByLicensePlate(licensePlate));
    }

    @GetMapping("/vehicles/{licensePlate}")
    public ResponseEntity<VehicleDTO> getVechicleByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(vehicleService.getVehicleByLicensePlate(licensePlate));
    }

    @GetMapping("/vehicles/byParams")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByParams(
            @RequestParam(value = "licensePlate", required = false) String licensePlate,
            @RequestParam(value = "make", required = false) String make,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "year", required = false) Integer year
    ) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByParams(licensePlate, make, model, year);
        return ResponseEntity.ok(vehicles);
    }
}