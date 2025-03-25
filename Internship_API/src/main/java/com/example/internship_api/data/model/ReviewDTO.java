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
public class ReviewDTO {
    private Long id;
    private Integer value;
    private String description;
    private LocalDateTime addingDate;
    private ClientDTO client;
    private DriverDTO driver;
}
