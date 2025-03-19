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
@Table(name="statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int numberOfHours;

    @Column
    private int numberOfClients;

    @Column
    private double priceAmount;

    @Column
    private LocalDateTime beginningOfWork;

    @Column
    private LocalDateTime endOfWork;

    @ManyToOne
    @JoinColumn(name = "driver_id",referencedColumnName = "id")
    private Driver driver;
}
