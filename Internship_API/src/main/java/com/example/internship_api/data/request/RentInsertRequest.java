package com.example.internship_api.data.request;

import java.time.LocalDateTime;

public record RentInsertRequest(
        LocalDateTime rentDate,
        LocalDateTime endDate,
        Long vehicle_id,
        Long client_id

) {
}
