package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DriverVehicleSearchObject extends BaseSearchObject{
    private Long driver_id;
    private Long vehicle_id;
    private LocalDateTime datePick;

}
