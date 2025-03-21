package com.example.internship_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String surname;

    @Column
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String passwordSalt;

    @Column
    private String passwordHash;

    @Column
    private String telephoneNumber;

    @Column
    private String gender;

    @Column
    private LocalDateTime registrationDate;

    @Column
    private boolean active;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Admin> admins;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Client> clients;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Driver>drivers;
}
