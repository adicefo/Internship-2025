package com.example.internship_api.data.request;

public record RouteInsertRequest(
        Double sourcePointLat,
        Double sourcePointLon,
        Double destinationPointLat,
        Double destinationPointLon,
        Long client_id,
        Long driver_id
) {
}
