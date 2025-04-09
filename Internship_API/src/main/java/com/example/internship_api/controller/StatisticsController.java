package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.ReviewDTO;
import com.example.internship_api.data.model.StatisticsDTO;
import com.example.internship_api.data.request.ReviewInsertRequest;
import com.example.internship_api.data.request.ReviewUpdateRequest;
import com.example.internship_api.data.request.StatisticsInsertRequest;
import com.example.internship_api.data.request.StatisticsUpdateRequest;
import com.example.internship_api.data.search_object.ReviewSearchObject;
import com.example.internship_api.data.search_object.StatisticsSearchObject;
import com.example.internship_api.service.StatisticsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/statistics")
//public class StatisticsController {
//
//    @Autowired
//    private StatisticsService service;
//
//    @GetMapping("/get")
//    public ResponseEntity<PagedResult<StatisticsDTO>> getAll(@ModelAttribute StatisticsSearchObject searchObject) {
//
//        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
//    }
//
//    @GetMapping("/getById/{id}")
//    public ResponseEntity<StatisticsDTO> getById(@PathVariable Long id) {
//
//        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
//    }
//
//    @PostMapping("/save")
//    @PreAuthorize("hasRole('driver') or hasRole('admin')")
//
//    public ResponseEntity<StatisticsDTO> save(@Valid @RequestBody StatisticsInsertRequest request) {
//
//        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
//    }
//    @PutMapping("/update/{id}")
//    @PreAuthorize("hasRole('driver')")
//
//    public ResponseEntity<StatisticsDTO> update(@PathVariable Long id,@Valid @RequestBody StatisticsUpdateRequest request) {
//
//        return new ResponseEntity<>(service.updateById(id,request),HttpStatus.OK);
//    }
//    @PutMapping("/updateFinish/{id}")
//    @PreAuthorize("hasRole('driver')")
//
//    public ResponseEntity<StatisticsDTO> update(@PathVariable Long id) {
//
//        return new ResponseEntity<>(service.updateFinish(id),HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<StatisticsDTO> delete(@PathVariable Long id) {
//        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
//    }
//}
