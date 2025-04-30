package com.example.clientservice.controller;

import com.example.clientservice.model.GetVehicles200Response;
import com.example.clientservice.model.VehicleDTO;
import com.example.clientservice.model.VehicleSearchObject;
import com.example.clientservice.model.VehicleUpsertRequest;
import com.example.clientservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @GetMapping("/get")
    public ResponseEntity<GetVehicles200Response> getVehicle(VehicleSearchObject search) {
       return new ResponseEntity<>(service.getVehicle(search), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getVehicleById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody @Valid VehicleUpsertRequest request) {
        return new ResponseEntity<>(service.createVehicle(request), HttpStatus.CREATED);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Integer id, @RequestBody @Valid VehicleUpsertRequest request) {
        return new ResponseEntity<>(service.updateVehicle(id, request), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<VehicleDTO> deleteVehicle(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteVehicle(id), HttpStatus.OK);
    }

}
