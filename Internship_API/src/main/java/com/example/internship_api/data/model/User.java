package com.example.internship_api.data.model;

import com.example.internship_api.entity.Admin;
import com.example.internship_api.entity.Client;
import com.example.internship_api.entity.Driver;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;

    private String name;

    private String surname;

    private String username;

    private String email;

    private String telephoneNumber;

    private String gender;

    private LocalDateTime registrationDate;

}
