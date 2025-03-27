package com.example.internship_api.data.request;

import java.time.LocalDateTime;

public record DriverVehicleFinishRequest(
        Long driver_id,
        LocalDateTime datePick
) {
}
