package com.example.internship_api.service;

import com.example.internship_api.dto.*;

import java.util.Map;

public interface RentService extends BaseCRUDService<RentDTO, RentSearchObject,
        RentInsertRequest, RentUpdateRequest>{
    RentDTO updateActive(Long id);
    RentDTO updateFinish(Long id);
    RentDTO updatePayment(Long id);
    Map<String,Boolean> checkAvailability(Long id, RentAvailabilityRequest request);
    Map<String,Double> getAmountForReport(GeneralReportRequest request);
}
