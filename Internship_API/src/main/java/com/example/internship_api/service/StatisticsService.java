package com.example.internship_api.service;

import com.example.internship_api.dto.*;

public interface StatisticsService extends BaseCRUDService<StatisticsDTO, StatisticsSearchObject,
        StatisticsInsertRequest, StatisticsUpdateRequest>{
    StatisticsDTO updateFinish(Long id);
}
