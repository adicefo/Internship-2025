package com.example.internship_api.data.request;

import java.time.LocalDateTime;

public record RentUpdateRequest(
        LocalDateTime endDate,
        Long vehicle_id
) {
}
