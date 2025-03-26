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
public class StatisticsDTO {
    private Long id;
    private Integer numberOfHours;
    private Integer numberOfClients;
    private Double priceAmount;
    private LocalDateTime beginningOfWork;
    private LocalDateTime endOfWork;
    private DriverDTO driver;
}
