package com.example.internship_api.delegate;

import com.example.internship_api.api.DriverVehicleApiDelegate;
import com.example.internship_api.dto.*;
import com.example.internship_api.service.DriverVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DriverVehicleDelegateImpl implements DriverVehicleApiDelegate {

    @Autowired
    private DriverVehicleService service;

    @Override
    public ResponseEntity<GetDriverVehicles200Response> getDriverVehicles(DriverVehicleSearchObject search) {
        GetDriverVehicles200Response response = new GetDriverVehicles200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DriverVehicleDTO> getDriverVehicleById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> checkIfDriverAssigned(Integer driverId) {
        return new ResponseEntity<>(service.checkIfAssigned(driverId.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DriverVehicleDTO> saveDriverVehicle(DriverVehicleInsertRequest driverVehicleInsertRequest) {
        return new ResponseEntity<>(service.save(driverVehicleInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DriverVehicleDTO> updateFinishDriverVehicle(DriverVehicleFinishRequest driverVehicleFinishRequest) {
        return new ResponseEntity<>(service.updateFinish(driverVehicleFinishRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DriverVehicleDTO> deleteDriverVehicle(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
