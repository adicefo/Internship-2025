package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.DriverVehicleDTO;
import com.example.internship_api.data.model.ReviewDTO;
import com.example.internship_api.data.request.DriverVehicleFinishRequest;
import com.example.internship_api.data.request.DriverVehicleInsertRequest;
import com.example.internship_api.data.request.ReviewInsertRequest;
import com.example.internship_api.data.search_object.DriverVehicleSearchObject;
import com.example.internship_api.data.search_object.ReviewSearchObject;
import com.example.internship_api.service.DriverVehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@RestController
//@RequestMapping("/driverVehicle")
//public class DriverVehicleController {
//    @Autowired
//    private DriverVehicleService service;
//
//    @GetMapping("/get")
//    public ResponseEntity<PagedResult<DriverVehicleDTO>> getAll(@ModelAttribute DriverVehicleSearchObject searchObject) {
//
//        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
//    }
//
//    @GetMapping("/getById/{id}")
//    public ResponseEntity<DriverVehicleDTO> getById(@PathVariable Long id) {
//
//        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
//    }
//    @GetMapping("/checkIfAssigned/{driver_id}")
//    public ResponseEntity<Map<String,Boolean>> checkIfAssigned(@PathVariable Long driver_id) {
//
//        return new ResponseEntity<>(service.checkIfAssigned(driver_id),HttpStatus.OK);
//    }
//
//    @PostMapping("/save")
//    public ResponseEntity<DriverVehicleDTO> save(@Valid @RequestBody DriverVehicleInsertRequest request) {
//
//        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
//    }
//    @PutMapping("/updateFinish")
//    public ResponseEntity<DriverVehicleDTO> updateFinish(@Valid @RequestBody DriverVehicleFinishRequest request) {
//
//        return new ResponseEntity<>(service.updateFinish(request),HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<DriverVehicleDTO> delete(@PathVariable Long id) {
//        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
//    }
//
//}
