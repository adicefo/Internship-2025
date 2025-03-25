package com.example.internship_api.data.request;

import lombok.NonNull;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record RentAvailabilityRequest(
        Long vehicle_id,
       @NonNull LocalDateTime rentDate,
        @NonNull LocalDateTime endDate
) {
}
