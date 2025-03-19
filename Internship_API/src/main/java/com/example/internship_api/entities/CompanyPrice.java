package com.example.internship_api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
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
