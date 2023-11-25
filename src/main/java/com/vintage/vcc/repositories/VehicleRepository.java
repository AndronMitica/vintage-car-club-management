package com.vintage.vcc.repositories;

import com.vintage.vcc.model.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByLicensePlate(String licensePlate);

    @Query(value = "SELECT * FROM vehicles v " +
            "WHERE (:licensePlate IS NULL OR v.license_plate = :licensePlate) " +
            "AND (:make IS NULL OR v.make = :make) " +
            "AND (:model IS NULL OR v.model = :model) " +
            "AND (:year IS NULL OR v.year = :year)",
            nativeQuery = true)
    List<Vehicle> findVehiclesByParams(
            @Param("licensePlate") String licensePlate,
            @Param("make") String make,
            @Param("model") String model,
            @Param("year") Integer year
    );
    void deleteByLicensePlate(String licensePlate);

    @Modifying
    @Query("UPDATE Vehicle v SET v.make = :make WHERE v.licensePlate = :licensePlate")
    void updateMakeByLicensePlate(@Param("make") String make, @Param("licensePlate") String licensePlate);

    @Modifying
    @Query("UPDATE Vehicle v SET v.model = :model, v.year = :year WHERE v.licensePlate = :licensePlate")
    void updateModelAndYearByLicensePlate(@Param("model") String model, @Param("year") int year, @Param("licensePlate") String licensePlate);
}