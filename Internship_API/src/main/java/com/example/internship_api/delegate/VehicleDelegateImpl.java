package com.example.internship_api.delegate;

import com.example.internship_api.api.VehicleApiDelegate;
import com.example.internship_api.dto.GetVehicles200Response;
import com.example.internship_api.dto.VehicleDTO;
import com.example.internship_api.dto.VehicleSearchObject;
import com.example.internship_api.dto.VehicleUpsertRequest;
import com.example.internship_api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VehicleDelegateImpl implements VehicleApiDelegate {
    @Autowired
    private VehicleService service;

    @Override
    public ResponseEntity<GetVehicles200Response> getVehicles(VehicleSearchObject search) {
        GetVehicles200Response response = new GetVehicles200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<VehicleDTO> getVehicleById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<VehicleDTO> saveVehicle(VehicleUpsertRequest vehicleUpsertRequest) {
        return new ResponseEntity<>(service.save(vehicleUpsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<VehicleDTO> updateVehicle(Integer id, VehicleUpsertRequest vehicleUpsertRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), vehicleUpsertRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VehicleDTO> deleteVehicle(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
