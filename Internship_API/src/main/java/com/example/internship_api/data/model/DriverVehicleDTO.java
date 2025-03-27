package com.example.internship_api.data.model;

import com.example.internship_api.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverVehicleDTO {
    private Long id;
    private LocalDateTime datePick;
    private LocalDateTime dateDrop;
    private VehicleDTO vehicle;
    private DriverDTO driver;

}
