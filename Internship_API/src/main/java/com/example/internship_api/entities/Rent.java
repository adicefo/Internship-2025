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
@Table(name="rent")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime rentDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private int numberOfDays;

    @Column
    private double fullPrice;

    @Column
    private boolean paid;

    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name = "vehicle_id",referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "client_id",referencedColumnName = "id")
    private Client client;


}
