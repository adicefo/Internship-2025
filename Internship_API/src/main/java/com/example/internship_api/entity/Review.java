package com.example.internship_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor
@Entity
@Table(name="review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false)
    private Integer value;

    @NonNull
    @Column(nullable = false)
    private String description;

    @Column
    private LocalDateTime addingDate;

    @ManyToOne
    @JoinColumn(name = "reviews_id",referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "reviewed_id",referencedColumnName = "id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "route_id",referencedColumnName = "id")
    private Route route;

}
