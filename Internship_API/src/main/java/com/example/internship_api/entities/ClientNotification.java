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
@Table(name="client_notification")
public class ClientNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime addingDate;
    @ManyToOne
    @JoinColumn(name = "client_id",referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "notification_id",referencedColumnName = "id")
    private Notification notification;




}
