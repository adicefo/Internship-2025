package com.example.internship_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="driver_notification")
public class DriverNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime addingDate;
    @ManyToOne
    @JoinColumn(name = "driver_id",referencedColumnName = "id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "notification_id",referencedColumnName = "id")
    private Notification notification;
}
