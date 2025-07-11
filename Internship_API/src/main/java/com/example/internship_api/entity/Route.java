package com.example.internship_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor
@Entity
@Table(name="route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Double sourcePointLat;

    @Column
    private Double sourcePointLon;

    @Column
    private Double destinationPointLat;

    @Column
    private Double destinationPointLon;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private Integer duration=0;

    @Column
    private Double numberOfKilometers;

    @Column
    private Double fullPrice;

    @Column
    private Boolean paid=false;

    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name = "client_id",referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "driver_id",referencedColumnName = "id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "companyPrice_id",referencedColumnName = "id")
    private CompanyPrice companyPrice;

    @OneToMany(mappedBy = "route",cascade = CascadeType.ALL)
    private Set<Request> requests;
    @OneToMany(mappedBy = "route",cascade = CascadeType.ALL)
    private Set<Review>reviews;

}
