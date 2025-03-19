package com.example.internship_api.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private boolean available;

    @Column
    private double averageFuelConsumption;

    @NonNull
    @Column(nullable = false)
    private String Name;

    @Column
    private byte[] image;

    @NonNull
    @Column(nullable = false)
    private double price;

    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL)
    private Set<DriverVehicle> driverVehicles;
    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL)
    private Set<Rent> rents;
}
