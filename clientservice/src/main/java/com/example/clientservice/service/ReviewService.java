package com.example.clientservice.service;

import com.example.clientservice.model.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface ReviewService {

GetReviews200Response getReviews(ReviewSearchObject search);
    ReviewDTO createReview(ReviewInsertRequest request);

    ReviewDTO updateReview(Integer id, ReviewUpdateRequest request);
    Map<String,Object> getDriversForReport(GeneralReportRequest request);

    ReviewDTO deleteReview(Integer id);

    ReviewDTO getReviewById(Integer id);
}
