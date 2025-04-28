package com.example.clientservice.controller;

import com.example.clientservice.model.*;
import com.example.clientservice.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService service;

    public DriverController(DriverService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<DriverDTO> createDriver(@RequestBody @Valid UserInsertRequest userInsertRequest) {
        return new ResponseEntity<>(service.createDriver(userInsertRequest), HttpStatus.CREATED);
    }
    @PostMapping("/saveBasedOnUser/{userId}")
    public ResponseEntity<DriverDTO> createClientBasedOnUser(@PathVariable Integer userId) {
        return new ResponseEntity<>(service.createDriverBasedOnUser(userId), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DriverDTO> deleteClient(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteDriver(id), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DriverDTO> getClientById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getDriverById(id), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<GetDriver200Response> getClient(@RequestParam(required = false) DriverSearchObject searchObject) {
        return new ResponseEntity<>(service.getDriver(searchObject), HttpStatus.OK);
    }
}
