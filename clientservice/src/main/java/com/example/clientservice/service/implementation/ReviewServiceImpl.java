package com.example.clientservice.service.implementation;

import com.example.clientservice.api.ReviewApiClient;
import com.example.clientservice.model.*;
import com.example.clientservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewApiClient reviewApiClient;
    @Override
    public GetReviews200Response getReviews(ReviewSearchObject search) {
        ResponseEntity<GetReviews200Response> response = reviewApiClient.getReviews(search);
        return response.getBody();
    }

    @Override
    public ReviewDTO createReview(ReviewInsertRequest request) {
        ResponseEntity<ReviewDTO> response= reviewApiClient.saveReview(request);
        return response.getBody();
    }

    @Override
    public ReviewDTO updateReview(Integer id, ReviewUpdateRequest request) {
        ResponseEntity<ReviewDTO> response= reviewApiClient.updateReview(id, request);
        return response.getBody();
    }

    @Override
    public Map<String, Object> getDriversForReport(GeneralReportRequest request) {
       ResponseEntity<Map<String, Object>> response= reviewApiClient.getDriversForReport(request);
        return response.getBody();
    }

    @Override
    public ReviewDTO deleteReview(Integer id) {
        ResponseEntity<ReviewDTO> response= reviewApiClient.deleteReview(id);
        return response.getBody();
    }

    @Override
    public ReviewDTO getReviewById(Integer id) {
        ResponseEntity<ReviewDTO> response= reviewApiClient.getReviewById(id);
        return response.getBody();
    }
}
