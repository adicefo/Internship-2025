package com.example.internship_api.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
private Long id;
private String name;
private Boolean available;
private Double averageFuelConsumption;
private Byte[] image;
private Double price;
}
