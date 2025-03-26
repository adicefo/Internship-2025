package com.example.internship_api.service;

import com.example.internship_api.data.model.StatisticsDTO;
import com.example.internship_api.data.request.StatisticsInsertRequest;
import com.example.internship_api.data.request.StatisticsUpdateRequest;
import com.example.internship_api.data.search_object.StatisticsSearchObject;

public interface StatisticsService extends BaseCRUDService<StatisticsDTO, StatisticsSearchObject,
        StatisticsInsertRequest, StatisticsUpdateRequest>{
    StatisticsDTO updateFinish(Long id);
}
