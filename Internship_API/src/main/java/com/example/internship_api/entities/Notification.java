package com.example.internship_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private byte[] image;

    @Column
    private LocalDateTime addingDate;

    @Column
    private boolean forClient;
    @OneToMany(mappedBy = "notification")
    private Set<ClientNotification> clientNotifications;
    @OneToMany(mappedBy = "driver")
    private Set<DriverNotification> driverNotifications;

}
