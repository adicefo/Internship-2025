package com.example.internship_api.repository;

import com.example.internship_api.entity.DriverVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverVehicleRepository extends JpaRepository<DriverVehicle,Long> {
}
