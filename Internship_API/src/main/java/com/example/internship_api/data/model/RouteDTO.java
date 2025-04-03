package com.example.internship_api.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private Long id;
    private Double sourcePointLat;
    private Double sourcePointLon;
    private Double destinationPointLat;
    private Double destinationPointLon;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer duration=null;
    private Double numberOfKilometers;
    private Double fullPrice;
    private Boolean paid;
    private String status;
    private ClientDTO client;
    private DriverDTO driver;

}
