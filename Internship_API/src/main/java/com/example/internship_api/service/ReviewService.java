package com.example.internship_api.service;

import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.model.ReviewDTO;
import com.example.internship_api.data.request.*;
import com.example.internship_api.data.search_object.RentSearchObject;
import com.example.internship_api.data.search_object.ReviewSearchObject;

import java.util.List;
import java.util.Map;

public interface ReviewService extends BaseCRUDService<ReviewDTO, ReviewSearchObject,
        ReviewInsertRequest, ReviewUpdateRequest>{
    List<Map<String,Object>> getDriversForReport(GeneralReportRequest request);
}
