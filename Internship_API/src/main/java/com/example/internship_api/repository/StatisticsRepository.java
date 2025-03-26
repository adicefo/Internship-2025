package com.example.internship_api.repository;

import com.example.internship_api.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics,Long> {
}
