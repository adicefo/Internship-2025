package com.example.internship_api.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // <-- Add this!
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private byte[] image;
    private UserDTO user;
}
