package com.example.internship_api.delegate;

import com.example.internship_api.api.ReviewApiDelegate;
import com.example.internship_api.dto.*;
import com.example.internship_api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReviewDelegateImpl implements ReviewApiDelegate {

    @Autowired
    private ReviewService service;

    @Override
    public ResponseEntity<GetReviews200Response> getReviews(ReviewSearchObject search) {
        GetReviews200Response response = new GetReviews200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ReviewDTO> getReviewById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReviewDTO> saveReview(ReviewInsertRequest reviewInsertRequest) {
        return new ResponseEntity<>(service.save(reviewInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getDriversForReport(GeneralReportRequest generalReportRequest) {
        return new ResponseEntity<>(service.getDriversForReport(generalReportRequest), HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<List<ClientReviewAverageDTO>> getAverageReview()
    {
        return new ResponseEntity<>(service.getAverageReviewPerClient(), HttpStatus.OK);

    }
    @Override
    public ResponseEntity<ReviewDTO> updateReview(Integer id, ReviewUpdateRequest reviewUpdateRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), reviewUpdateRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReviewDTO> deleteReview(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
