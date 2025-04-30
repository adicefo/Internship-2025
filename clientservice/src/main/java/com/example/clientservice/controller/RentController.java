package com.example.clientservice.controller;

import com.example.clientservice.model.*;
import com.example.clientservice.service.RentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rent")
public class RentController {
    @Autowired
    private RentService service;

    @GetMapping("/get")
    public ResponseEntity<GetRents200Response> getRents(RentSearchObject search)
    {
        return new ResponseEntity<>(service.getRent(search), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<RentDTO> getRentById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getRentById(id), HttpStatus.OK);
    }

    @PostMapping("/checkAvailability/{id}")
    public ResponseEntity<Map<String,Boolean>> checkAvailability(@PathVariable Integer id, @RequestBody @Valid RentAvailabilityRequest request) {
        return new ResponseEntity<>(service.checkRentAvailability(id, request), HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<RentDTO> createRent(@RequestBody @Valid RentInsertRequest request) {
        return new ResponseEntity<>(service.createRent(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RentDTO> updateRent(@PathVariable Integer id, @RequestBody @Valid RentUpdateRequest request) {
        return new ResponseEntity<>(service.updateRent(id, request), HttpStatus.OK);
    }

    @PutMapping("/updateActive/{id}")
    public ResponseEntity<RentDTO> updateActive(@PathVariable Integer id) {
        return new ResponseEntity<>(service.updateRentActive(id), HttpStatus.OK);
    }

    @PutMapping("/updateFinish/{id}")
    public ResponseEntity<RentDTO> updateFinish(@PathVariable Integer id) {
        return new ResponseEntity<>(service.updateRentFinish(id), HttpStatus.OK);
    }
    @PutMapping("/updatePayment/{id}")
    public ResponseEntity<RentDTO> updatePayment(@PathVariable Integer id) {
        return new ResponseEntity<>(service.updateRentPayment(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RentDTO> deleteRent(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteRent(id), HttpStatus.OK);
    }
}
