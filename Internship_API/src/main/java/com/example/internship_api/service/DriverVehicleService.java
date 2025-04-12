package com.example.internship_api.service;

import com.example.internship_api.dto.*;

import java.util.Map;

public interface DriverVehicleService extends BaseCRUDService<DriverVehicleDTO, DriverVehicleSearchObject,
        DriverVehicleInsertRequest, DriverVehicleUpdateRequest>{
    Map<String,Boolean> checkIfAssigned(Long driver_id);
    DriverVehicleDTO updateFinish(DriverVehicleFinishRequest request);
}
