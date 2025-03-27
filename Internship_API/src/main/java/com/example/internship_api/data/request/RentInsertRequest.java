package com.example.internship_api.data.request;

import lombok.NonNull;

import java.time.LocalDateTime;

public record RentInsertRequest(
      @NonNull LocalDateTime rentDate,
       @NonNull LocalDateTime endDate,
        Long vehicle_id,
        Long client_id

) {
}
