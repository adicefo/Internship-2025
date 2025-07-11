package com.example.internship_api.service;

import com.example.internship_api.dto.*;

import java.util.List;
import java.util.Map;

public interface ReviewService extends BaseCRUDService<ReviewDTO, ReviewSearchObject,
        ReviewInsertRequest, ReviewUpdateRequest>{
    Map<String,Object> getDriversForReport(GeneralReportRequest request);
    List<ClientReviewAverageDTO> getAverageReviewPerClient();
}
