package com.example.internship_api.delegate;

import com.example.internship_api.api.RouteApiDelegate;
import com.example.internship_api.dto.*;
import com.example.internship_api.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class RouteDelegateImpl implements RouteApiDelegate {

    @Autowired
    private RouteService service;

    @Override
    public ResponseEntity<GetRoutes200Response> getRoutes(RouteSearchObject search) {
        GetRoutes200Response response = new GetRoutes200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RouteDTO> getRouteById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Double>> getAmountForReport(GeneralReportRequest generalReportRequest) {
        return new ResponseEntity<>(service.getAmountForReport(generalReportRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RouteDTO> saveRoute(RouteInsertRequest routeInsertRequest) {
        return new ResponseEntity<>(service.save(routeInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RouteDTO> updateRoute(Integer id, RouteUpdateRequest routeUpdateRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), routeUpdateRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RouteDTO> updateRouteFinish(Integer id) {
        return new ResponseEntity<>(service.updateFinish(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RouteDTO> updateRoutePayment(Integer id) {
        return new ResponseEntity<>(service.updatePayment(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RouteDTO> deleteRoute(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
