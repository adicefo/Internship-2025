package com.example.internship_api.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverDTO {
    private Long id;
    private Integer numberOfClientsAmount;
    private Integer numberOfHoursAmount;
    private UserDTO user;
}
