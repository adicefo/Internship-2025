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
public class RentDTO {
    private Long id;
    private LocalDateTime rentDate;
    private LocalDateTime endDate;
    private Integer numberOfDays;
    private Double fullPrice;
    private Boolean paid=null;
    private String status;
    private VehicleDTO vehicle;
    private ClientDTO client;
}
