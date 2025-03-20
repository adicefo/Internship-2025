package com.example.internship_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor
@Entity
@Table(name="companyDetails")
public class CompanyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "companyPrices_id",referencedColumnName = "id")
    private CompanyPrice companyPrice;
}
