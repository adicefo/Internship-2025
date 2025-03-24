package com.example.internship_api.data.request;


public record VehicleUpsertRequest(
        String name,
        Boolean available,
        Double averageFuelConsumption,
        Byte[] image,
        Double price
) {
}
