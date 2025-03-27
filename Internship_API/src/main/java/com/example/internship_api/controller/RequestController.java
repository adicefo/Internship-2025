package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.NotificationDTO;
import com.example.internship_api.data.model.RequestDTO;
import com.example.internship_api.data.request.NotificationUpsertRequest;
import com.example.internship_api.data.request.RequestInsertRequest;
import com.example.internship_api.data.request.RequestUpdateRequest;
import com.example.internship_api.data.search_object.NotificationSearchObject;
import com.example.internship_api.data.search_object.RequestSearchObject;
import com.example.internship_api.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService service;
    @GetMapping("/get")
    public ResponseEntity<PagedResult<RequestDTO>> getAll(@ModelAttribute RequestSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<RequestDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<RequestDTO> save(@Valid @RequestBody RequestInsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<RequestDTO> update(@PathVariable Long id, @Valid @RequestBody RequestUpdateRequest request) {

        return new ResponseEntity<>(service.updateById(id,request),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RequestDTO> delete(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
