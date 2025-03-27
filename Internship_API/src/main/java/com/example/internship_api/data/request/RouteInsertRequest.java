package com.example.internship_api.data.request;

import org.hibernate.validator.constraints.Range;

public record RouteInsertRequest(
      @Range(min = -90,max=90,message = "Latitude must be between -90 and 90 degrees") Double sourcePointLat,
      @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180 degrees")   Double sourcePointLon,
      @Range(min = -90,max=90,message = "Latitude must be between -90 and 90 degrees")  Double destinationPointLat,
      @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180 degrees")    Double destinationPointLon,
        Long client_id,
        Long driver_id
) {
}
