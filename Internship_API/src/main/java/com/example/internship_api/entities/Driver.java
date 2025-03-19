package com.example.internship_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
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
