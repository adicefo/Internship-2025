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
@Table(name="companyPrice")
public class CompanyPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false)
    private double pricePerKilometer;

    @Column
    private LocalDateTime addingDate;

    @OneToMany(mappedBy = "companyPrice",cascade = CascadeType.ALL)
    private Set<CompanyDetails> companyDetails;

    @OneToMany(mappedBy = "companyPrice",cascade = CascadeType.ALL)
    private Set<Route> routes;
}
