package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.NotificationDTO;
import com.example.internship_api.data.model.ReviewDTO;
import com.example.internship_api.data.request.NotificationUpsertRequest;
import com.example.internship_api.data.request.ReviewInsertRequest;
import com.example.internship_api.data.request.ReviewUpdateRequest;
import com.example.internship_api.data.search_object.NotificationSearchObject;
import com.example.internship_api.data.search_object.ReviewSearchObject;
import com.example.internship_api.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService service;

    @GetMapping("/get")
    public ResponseEntity<PagedResult<ReviewDTO>> getAll(@ModelAttribute ReviewSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ReviewDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ReviewDTO> createUser(@Valid @RequestBody ReviewInsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewDTO> updateUser(@PathVariable Long id,@Valid @RequestBody ReviewUpdateRequest request) {

        return new ResponseEntity<>(service.updateById(id,request),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ReviewDTO> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
