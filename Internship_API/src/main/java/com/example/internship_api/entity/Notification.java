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
@Table(name="notification")
public class    Notification {
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
    private Boolean forClient;
    @OneToMany(mappedBy = "notification",cascade = CascadeType.ALL)
    private Set<ClientNotification> clientNotifications;
    @OneToMany(mappedBy = "notification",cascade = CascadeType.ALL)
    private Set<DriverNotification> driverNotifications;

}
