package com.vintage.vcc.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintage.vcc.exceptions.VehicleNotFoundException;
import com.vintage.vcc.model.dtos.VehicleDTO;
import com.vintage.vcc.model.entities.Vehicle;
import com.vintage.vcc.repositories.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleServiceImpl;

    @Test
    public void testGetVehicleByLicensePlate_Success() throws VehicleNotFoundException {
        // Given
        String licensePlate = "ABC123";
        Vehicle vehicleEntity = new Vehicle();
        vehicleEntity.setLicensePlate(licensePlate);
        VehicleDTO expectedDTO = new VehicleDTO(); // Set expected DTO values here

        when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(vehicleEntity);
        when(objectMapper.convertValue(vehicleEntity, VehicleDTO.class)).thenReturn(expectedDTO);

        // When
        VehicleDTO resultDTO = vehicleServiceImpl.getVehicleByLicensePlate(licensePlate);

        // Then
        assertNotNull(resultDTO);
        assertEquals(expectedDTO, resultDTO);

        verify(vehicleRepository, times(1)).findByLicensePlate(licensePlate);
        verify(objectMapper, times(1)).convertValue(vehicleEntity, VehicleDTO.class);
    }

    @Test
    public void testGetVehicleByLicensePlate_VehicleNotFound() {
        // Given
        String licensePlate = "XYZ789";

        when(vehicleRepository.findByLicensePlate(anyString())).thenReturn(null);

        // When and Then
        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () ->
                vehicleServiceImpl.getVehicleByLicensePlate(licensePlate));

        verify(vehicleRepository, times(1)).findByLicensePlate(licensePlate);

        verifyNoInteractions(objectMapper);

        assertEquals("Vehicle with license plate: " + licensePlate + " not found", exception.getMessage());
    }
}