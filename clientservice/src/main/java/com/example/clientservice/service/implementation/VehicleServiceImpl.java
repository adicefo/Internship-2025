package com.example.clientservice.service.implementation;

import com.example.clientservice.api.VehicleApiClient;
import com.example.clientservice.model.GetVehicles200Response;
import com.example.clientservice.model.VehicleDTO;
import com.example.clientservice.model.VehicleSearchObject;
import com.example.clientservice.model.VehicleUpsertRequest;
import com.example.clientservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleApiClient vehicleApiClient;
    @Override
    public VehicleDTO createVehicle(VehicleUpsertRequest request) {
        ResponseEntity<VehicleDTO> response= vehicleApiClient.saveVehicle(request);
        return response.getBody();
    }

    @Override
    public VehicleDTO deleteVehicle(Integer id) {
        ResponseEntity<VehicleDTO> response = vehicleApiClient.deleteVehicle(id);
        return response.getBody();
    }

    @Override
    public VehicleDTO getVehicleById(Integer id) {
        ResponseEntity<VehicleDTO> response = vehicleApiClient.getVehicleById(id);
        return response.getBody();
    }

    @Override
    public GetVehicles200Response getVehicle(VehicleSearchObject search) {
        ResponseEntity<GetVehicles200Response> response = vehicleApiClient.getVehicles(search);
        return response.getBody();
    }

    @Override
    public VehicleDTO updateVehicle(Integer id, VehicleUpsertRequest vehicleUpdateRequest) {
       ResponseEntity<VehicleDTO> response = vehicleApiClient.updateVehicle(id, vehicleUpdateRequest);
        return response.getBody();
    }
}
