package com.example.clientservice.controller;

import com.example.clientservice.model.*;
import com.example.clientservice.service.RouteService;
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
    public ResponseEntity<GetRoutes200Response> getRoutes(RouteSearchObject search) {
        return new ResponseEntity<>(service.getRoute(search), HttpStatus.OK);

    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<RouteDTO> getRouteById(Integer id) {
        return new ResponseEntity<>(service.getRouteById(id), HttpStatus.OK);
    }
    @GetMapping("/getAmountForReport")
    public ResponseEntity<Map<String ,Double>> getAmountForReport(GeneralReportRequest request) {
        return new ResponseEntity<>(service.getAmountForReport(request), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<RouteDTO> createRoute(@RequestBody @Valid RouteInsertRequest request) {
        return new ResponseEntity<>(service.createRoute(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RouteDTO> updateRoute(@PathVariable Integer id, @RequestBody @Valid RouteUpdateRequest request) {
        return new ResponseEntity<>(service.updateRoute(id, request), HttpStatus.OK);
    }
    @PutMapping("/updateFinish/{id}")
    public ResponseEntity<RouteDTO> updateFinish(@PathVariable Integer id) {
        return new ResponseEntity<>(service.updateFinish(id), HttpStatus.OK);
    }
    @PutMapping("/updatePayment/{id}")
    public ResponseEntity<RouteDTO> updatePayment(@PathVariable Integer id) {
        return new ResponseEntity<>(service.updatePayment(id), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RouteDTO> deleteRoute(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteRoute(id), HttpStatus.OK);
    }
}
