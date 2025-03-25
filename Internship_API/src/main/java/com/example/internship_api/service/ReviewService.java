package com.example.internship_api.service;

import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.model.ReviewDTO;
import com.example.internship_api.data.request.RentInsertRequest;
import com.example.internship_api.data.request.RentUpdateRequest;
import com.example.internship_api.data.request.ReviewInsertRequest;
import com.example.internship_api.data.request.ReviewUpdateRequest;
import com.example.internship_api.data.search_object.RentSearchObject;
import com.example.internship_api.data.search_object.ReviewSearchObject;

public interface ReviewService extends BaseCRUDService<ReviewDTO, ReviewSearchObject,
        ReviewInsertRequest, ReviewUpdateRequest>{

}
