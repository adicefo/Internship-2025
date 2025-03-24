package com.example.internship_api.repository;

import com.example.internship_api.entity.CompanyPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyPriceRepository extends JpaRepository<CompanyPrice, Long> {
    CompanyPrice findFirstByOrderByIdDesc();

}
