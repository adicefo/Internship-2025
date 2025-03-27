package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.DriverDTO;
import com.example.internship_api.data.model.RouteDTO;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.request.*;
import com.example.internship_api.data.search_object.DriverSearchObject;
import com.example.internship_api.data.search_object.RouteSearchObject;
import com.example.internship_api.entity.Route;
import com.example.internship_api.service.DriverService;
import com.example.internship_api.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private RouteService service;

    @GetMapping("/get")
    public ResponseEntity<PagedResult<RouteDTO>> getAll(@ModelAttribute RouteSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<RouteDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }
    @GetMapping("/getAmountForReport")
    public ResponseEntity<Map<String,Double>> getAmountForReport(@Valid @RequestBody GeneralReportRequest request) {

        return new ResponseEntity<>(service.getAmountForReport(request),HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<RouteDTO> save(@Valid @RequestBody RouteInsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<RouteDTO> update(@PathVariable Long id,@Valid @RequestBody RouteUpdateRequest request) {

        return new ResponseEntity<>(service.updateById(id,request),HttpStatus.OK);
    }
    @PutMapping("/updateFinish/{id}")
    public ResponseEntity<RouteDTO> updateFinish(@PathVariable Long id) {

        return new ResponseEntity<>(service.updateFinish(id),HttpStatus.OK);
    }
    @PutMapping("/updatePayment/{id}")
    public ResponseEntity<RouteDTO> updatePayment(@PathVariable Long id) {

        return new ResponseEntity<>(service.updatePayment(id),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RouteDTO> delete(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
