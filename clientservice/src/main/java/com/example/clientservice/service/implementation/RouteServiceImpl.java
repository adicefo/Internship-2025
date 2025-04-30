package com.example.clientservice.service.implementation;

import com.example.clientservice.api.RouteApiClient;
import com.example.clientservice.model.*;
import com.example.clientservice.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteApiClient routeApiClient;
    @Override
    public RouteDTO createRoute(RouteInsertRequest request) {
        ResponseEntity<RouteDTO> response = routeApiClient.saveRoute(request);
        return response.getBody();
    }

    @Override
    public RouteDTO deleteRoute(Integer id) {
        ResponseEntity<RouteDTO> response = routeApiClient.deleteRoute(id);
        return response.getBody();
    }

    @Override
    public RouteDTO getRouteById(Integer id) {
        ResponseEntity<RouteDTO> response = routeApiClient.getRouteById(id);
        return response.getBody();
    }

    @Override
    public GetRoutes200Response getRoute(RouteSearchObject search) {
        ResponseEntity<GetRoutes200Response> response = routeApiClient.getRoutes(search);
        return response.getBody();
    }

    @Override
    public RouteDTO updateRoute(Integer id, RouteUpdateRequest request) {
        ResponseEntity<RouteDTO> response = routeApiClient.updateRoute(id, request);
        return response.getBody();
    }

    @Override
    public Map<String, Double> getAmountForReport(GeneralReportRequest request) {
        return routeApiClient.getAmountForReport(request).getBody();
    }

    @Override
    public RouteDTO updateFinish(Integer id) {
        ResponseEntity<RouteDTO> response = routeApiClient.updateRouteFinish(id);
        return response.getBody();
    }

    @Override
    public RouteDTO updatePayment(Integer id) {
        ResponseEntity<RouteDTO> response = routeApiClient.updateRoutePayment(id);
        return response.getBody();
    }
}
