package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.ClientDTO;
import com.example.internship_api.data.model.DriverDTO;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.search_object.ClientSearchObject;
import com.example.internship_api.data.search_object.DriverSearchObject;
import com.example.internship_api.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {
    @Autowired
    private DriverService service;

    @GetMapping("/get")
    public ResponseEntity<PagedResult<DriverDTO>> getAll(@ModelAttribute DriverSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DriverDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<DriverDTO> save(@Valid @RequestBody UserInsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @PostMapping("/save/{userId}")
    public ResponseEntity<DriverDTO> save(@PathVariable Long userId) {

        return new ResponseEntity<>(service.saveBasedOnUser(userId),HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DriverDTO> delete(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
