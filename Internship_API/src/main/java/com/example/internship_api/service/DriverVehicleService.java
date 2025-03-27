package com.example.internship_api.service;

import com.example.internship_api.data.model.DriverVehicleDTO;
import com.example.internship_api.data.request.DriverVehicleFinishRequest;
import com.example.internship_api.data.request.DriverVehicleInsertRequest;
import com.example.internship_api.data.request.DriverVehicleUpdateRequest;
import com.example.internship_api.data.search_object.DriverVehicleSearchObject;

import java.util.Map;

public interface DriverVehicleService extends BaseCRUDService<DriverVehicleDTO, DriverVehicleSearchObject,
        DriverVehicleInsertRequest, DriverVehicleUpdateRequest>{
    Map<String,Boolean> checkIfAssigned(Long driver_id);
    DriverVehicleDTO updateFinish(DriverVehicleFinishRequest request);
}
