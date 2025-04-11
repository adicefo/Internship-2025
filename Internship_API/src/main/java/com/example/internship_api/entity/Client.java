package com.example.internship_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor
@Entity
@Table(name="client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column
    private byte[] image;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private Set<ClientNotification> clientNotifications;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private Set<Rent> rents;
    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private Set<Review> reviews;
    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private Set<Route> routes;

}
