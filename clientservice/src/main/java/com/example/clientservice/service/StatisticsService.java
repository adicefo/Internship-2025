package com.example.clientservice.service;

import com.example.clientservice.model.*;

public interface StatisticsService {
    StatisticsDTO deleteStatistics(Integer id);
    StatisticsDTO getStatisticsById(Integer id);
    StatisticsDTO updateStatistics(Integer id, StatisticsUpdateRequest request);
    StatisticsDTO createStatistics(StatisticsInsertRequest request);
    StatisticsDTO updateStatisticsFinish(Integer id);
    GetStatistics200Response getStatistics(StatisticsSearchObject search);
}
