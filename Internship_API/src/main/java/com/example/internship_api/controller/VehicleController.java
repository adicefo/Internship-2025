package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.RouteDTO;
import com.example.internship_api.data.model.VehicleDTO;
import com.example.internship_api.data.request.RouteInsertRequest;
import com.example.internship_api.data.request.RouteUpdateRequest;
import com.example.internship_api.data.request.VehicleUpsertRequest;
import com.example.internship_api.data.search_object.RouteSearchObject;
import com.example.internship_api.data.search_object.VehicleSearchObject;
import com.example.internship_api.service.VehicleService;
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
    public ResponseEntity<PagedResult<VehicleDTO>> getAll(@ModelAttribute VehicleSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<VehicleDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<VehicleDTO> save(@RequestBody VehicleUpsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable Long id, @RequestBody VehicleUpsertRequest request) {

        return new ResponseEntity<>(service.updateById(id,request),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<VehicleDTO> delete(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
