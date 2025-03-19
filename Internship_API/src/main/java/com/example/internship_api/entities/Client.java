package com.example.internship_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private byte[] image;


    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
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
