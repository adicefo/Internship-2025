package com.example.internship_api.delegate;

import com.example.internship_api.api.StatisticsApiDelegate;
import com.example.internship_api.dto.*;
import com.example.internship_api.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StatisticsDelegateImpl implements StatisticsApiDelegate {

    @Autowired
    private StatisticsService service;

    @Override
    public ResponseEntity<GetStatistics200Response> getStatistics(StatisticsSearchObject search) {
        GetStatistics200Response response = new GetStatistics200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<StatisticsDTO> getStatisticsById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StatisticsDTO> saveStatistics(StatisticsInsertRequest statisticsInsertRequest) {
        return new ResponseEntity<>(service.save(statisticsInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<StatisticsDTO> updateStatistics(Integer id, StatisticsUpdateRequest statisticsUpdateRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), statisticsUpdateRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StatisticsDTO> updateStatisticsFinish(Integer id) {
        return new ResponseEntity<>(service.updateFinish(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StatisticsDTO> deleteStatistics(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }

}
