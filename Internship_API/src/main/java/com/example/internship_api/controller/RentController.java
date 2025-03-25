package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.model.RouteDTO;
import com.example.internship_api.data.request.*;
import com.example.internship_api.data.search_object.RentSearchObject;
import com.example.internship_api.data.search_object.RouteSearchObject;
import com.example.internship_api.service.RentService;
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
    public ResponseEntity<PagedResult<RentDTO>> getAll(@ModelAttribute RentSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<RentDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<RentDTO> save(@RequestBody RentInsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @PostMapping("/checkAvailability/{id}")
    public ResponseEntity<Map<String,Boolean>>checkAvailability(@PathVariable Long id, @RequestBody RentAvailabilityRequest request){
        return new ResponseEntity<>(service.checkAvailability(id,request),HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<RentDTO> update(@PathVariable Long id, @RequestBody RentUpdateRequest request) {

        return new ResponseEntity<>(service.updateById(id,request),HttpStatus.OK);
    }
    @PutMapping("/updateActive/{id}")
    public ResponseEntity<RentDTO> updateActive(@PathVariable Long id) {

        return new ResponseEntity<>(service.updateActive(id),HttpStatus.OK);
    }
    @PutMapping("/updateFinish/{id}")
    public ResponseEntity<RentDTO> updateFinish(@PathVariable Long id) {

        return new ResponseEntity<>(service.updateFinish(id),HttpStatus.OK);
    }
    @PutMapping("/updatePayment/{id}")
    public ResponseEntity<RentDTO> updatePayment(@PathVariable Long id) {

        return new ResponseEntity<>(service.updatePayment(id),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RentDTO> delete(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
