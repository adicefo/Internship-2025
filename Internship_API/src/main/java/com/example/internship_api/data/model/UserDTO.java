package com.example.internship_api.data.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // <-- Add this!
@AllArgsConstructor
public class UserDTO {
   private Long id;

   private String name;

   private String surname;

   private String username;

    private String email;

   private String telephoneNumber;

   private String gender;

   private LocalDateTime registrationDate;


}
