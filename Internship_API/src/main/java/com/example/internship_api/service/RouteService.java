package com.example.internship_api.service;

import com.example.internship_api.dto.*;

import java.util.List;
import java.util.Map;

public interface RouteService extends BaseCRUDService<RouteDTO, RouteSearchObject,
        RouteInsertRequest, RouteUpdateRequest> {
    RouteDTO updateFinish(Long id);
    RouteDTO updatePayment(Long id);
    Map<String,Double> getAmountForReport(GeneralReportRequest request);
    List<RouteClientCountDTO> getTopClientsByRoute();
}
