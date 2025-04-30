package com.example.clientservice.controller;

import com.example.clientservice.model.*;
import com.example.clientservice.service.StatisticsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService service;

    public StatisticsController(StatisticsService service) {
        this.service = service;
    }

    @GetMapping("/get")
    public ResponseEntity<GetStatistics200Response> getStatistics(StatisticsSearchObject search) {
        return new ResponseEntity<>(service.getStatistics(search), HttpStatus.OK);

    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<StatisticsDTO> getStatisticsById(Integer id) {
        return new ResponseEntity<>(service.getStatisticsById(id), HttpStatus.OK);
    }


    @PostMapping("/save")
    public ResponseEntity<StatisticsDTO> createStatistics(@RequestBody @Valid StatisticsInsertRequest request) {
        return new ResponseEntity<>(service.createStatistics(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StatisticsDTO> updateStatistics(@PathVariable Integer id, @RequestBody @Valid StatisticsUpdateRequest request) {
        return new ResponseEntity<>(service.updateStatistics(id, request), HttpStatus.OK);
    }
    @PutMapping("/updateFinish/{id}")
    public ResponseEntity<StatisticsDTO> updateFinish(@PathVariable Integer id) {
        return new ResponseEntity<>(service.updateStatisticsFinish(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StatisticsDTO> deleteStatistics(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteStatistics(id), HttpStatus.OK);
    }
}
