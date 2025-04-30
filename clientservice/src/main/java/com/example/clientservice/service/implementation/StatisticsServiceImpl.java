package com.example.clientservice.service.implementation;

import com.example.clientservice.api.StatisticsApiClient;
import com.example.clientservice.model.*;
import com.example.clientservice.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsApiClient apiClient;
    @Override
    public StatisticsDTO deleteStatistics(Integer id) {
        ResponseEntity<StatisticsDTO>response=apiClient.deleteStatistics(id);
        return response.getBody();

    }

    @Override
    public StatisticsDTO getStatisticsById(Integer id) {
        ResponseEntity<StatisticsDTO>response=apiClient.getStatisticsById(id);
        return response.getBody();
    }

    @Override
    public StatisticsDTO updateStatistics(Integer id, StatisticsUpdateRequest request) {
        ResponseEntity<StatisticsDTO>response=apiClient.updateStatistics(id, request);
        return response.getBody();
    }

    @Override
    public StatisticsDTO createStatistics(StatisticsInsertRequest request) {
        ResponseEntity<StatisticsDTO> response=apiClient.saveStatistics(request);
        return response.getBody();
    }

    @Override
    public StatisticsDTO updateStatisticsFinish(Integer id) {
        ResponseEntity<StatisticsDTO>response=apiClient.updateStatisticsFinish(id);
        return response.getBody();
    }

    @Override
    public GetStatistics200Response getStatistics(StatisticsSearchObject search) {
        ResponseEntity<GetStatistics200Response>response=apiClient.getStatistics(search);
        return response.getBody();
    }
}
