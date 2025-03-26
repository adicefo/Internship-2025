package com.example.internship_api.service;

import com.example.internship_api.data.model.RequestDTO;
import com.example.internship_api.data.model.StatisticsDTO;
import com.example.internship_api.data.request.RequestInsertRequest;
import com.example.internship_api.data.request.RequestUpdateRequest;
import com.example.internship_api.data.request.StatisticsInsertRequest;
import com.example.internship_api.data.request.StatisticsUpdateRequest;
import com.example.internship_api.data.search_object.RequestSearchObject;
import com.example.internship_api.data.search_object.StatisticsSearchObject;

public interface RequestService extends BaseCRUDService<RequestDTO, RequestSearchObject,
        RequestInsertRequest, RequestUpdateRequest>{

}
