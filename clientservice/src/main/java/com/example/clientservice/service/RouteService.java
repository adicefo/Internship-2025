package com.example.clientservice.service;

import com.example.clientservice.model.*;

import java.util.Map;

public interface RouteService {
    RouteDTO createRoute(RouteInsertRequest request);

    RouteDTO deleteRoute(Integer id);

    RouteDTO getRouteById(Integer id);

    GetRoutes200Response getRoute(RouteSearchObject search);

    RouteDTO updateRoute(Integer id, RouteUpdateRequest request);

    Map<String,Double> getAmountForReport(GeneralReportRequest request);

    RouteDTO updateFinish(Integer id);
    RouteDTO updatePayment(Integer id);
}
