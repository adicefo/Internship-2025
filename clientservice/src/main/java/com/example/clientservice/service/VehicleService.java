package com.example.clientservice.service;

import com.example.clientservice.model.GetVehicles200Response;
import com.example.clientservice.model.VehicleDTO;
import com.example.clientservice.model.VehicleSearchObject;
import com.example.clientservice.model.VehicleUpsertRequest;

public interface VehicleService {
    VehicleDTO createVehicle(VehicleUpsertRequest request);

    VehicleDTO deleteVehicle(Integer id);

    VehicleDTO getVehicleById(Integer id);

    GetVehicles200Response getVehicle(VehicleSearchObject search);

    VehicleDTO updateVehicle(Integer id, VehicleUpsertRequest vehicleUpdateRequest);

}
