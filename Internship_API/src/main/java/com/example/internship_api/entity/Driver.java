package com.example.internship_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor
@Entity
@Table(name="driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int numberOfHoursAmount;

    @Column
    private int numberOfClientsAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private Set<DriverVehicle> driverVehicle;
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private Set<DriverNotification> driverNotifications;
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private Set<Review> reviews;
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private Set<Request> requests;
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private Set<Route> routes;
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private Set<Statistics> statistics;

}
