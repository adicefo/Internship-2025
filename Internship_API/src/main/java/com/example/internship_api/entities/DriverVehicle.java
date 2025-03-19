package com.example.internship_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="driver_vehicle")
public class DriverVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime datePick;

    @Column
    private LocalDateTime dateDrop;

    @ManyToOne
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name="driver_id",referencedColumnName = "id")
    private Driver driver;


}
