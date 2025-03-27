package com.example.internship_api.data.request;

public record DriverVehicleInsertRequest(
        Long driver_id,
        Long vehicle_id
) {
}
