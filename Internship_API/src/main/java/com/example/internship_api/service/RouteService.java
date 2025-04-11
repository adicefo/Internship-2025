package com.example.internship_api.service;

import com.example.internship_api.dto.RouteDTO;
import com.example.internship_api.dto.*;
import com.example.internship_api.dto.RouteSearchObject;

import java.util.Map;

public interface RouteService extends BaseCRUDService<RouteDTO, RouteSearchObject,
        RouteInsertRequest, RouteUpdateRequest> {
    RouteDTO updateFinish(Long id);
    RouteDTO updatePayment(Long id);
    Map<String,Double> getAmountForReport(GeneralReportRequest request);
}
