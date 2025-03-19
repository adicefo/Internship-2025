package com.example.internship_api.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
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
