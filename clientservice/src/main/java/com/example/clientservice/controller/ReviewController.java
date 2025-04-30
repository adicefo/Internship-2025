package com.example.clientservice.controller;

import com.example.clientservice.model.*;
import com.example.clientservice.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService service;

    @GetMapping("/get")
    public ResponseEntity<GetReviews200Response> getReviews(ReviewSearchObject search)
    {
        return new ResponseEntity<>(service.getReviews(search), HttpStatus.OK);

    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ReviewDTO> getReviewById( @PathVariable  Integer id)
    {
        return new ResponseEntity<>(service.getReviewById(id), HttpStatus.OK);

    }

    @GetMapping("/getDriversForReport")
    public ResponseEntity<Map<String, Object>> getDriversForReport(@RequestBody @Valid GeneralReportRequest request)
    {
        return new ResponseEntity<>(service.getDriversForReport(request), HttpStatus.OK);

    }

    @PostMapping("/save")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody @Valid ReviewInsertRequest request)
    {
        return new ResponseEntity<>(service.createReview(request), HttpStatus.CREATED);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Integer id, @RequestBody @Valid ReviewUpdateRequest request)
    {
        return new ResponseEntity<>(service.updateReview(id, request), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Integer id)
    {
        return new ResponseEntity<>(service.deleteReview(id), HttpStatus.OK);

    }

}
