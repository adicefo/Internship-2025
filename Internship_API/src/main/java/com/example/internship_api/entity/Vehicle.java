package com.example.internship_api.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor
@Entity
@Table(name="vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Boolean available;

    @Column
    private Double averageFuelConsumption;

    @NonNull
    @Column(nullable = false)
    private String name;

    @Column
    private byte[] image;

    @NonNull
    @Column(nullable = false)
    private Double price;

    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL)
    private Set<DriverVehicle> driverVehicles;
    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL)
    private Set<Rent> rents;
}
