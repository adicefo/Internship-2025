package com.example.internship_api.service;

import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.model.RouteDTO;
import com.example.internship_api.data.request.*;
import com.example.internship_api.data.search_object.RentSearchObject;
import com.example.internship_api.data.search_object.RouteSearchObject;

import java.util.Map;

public interface RentService extends BaseCRUDService<RentDTO, RentSearchObject,
        RentInsertRequest, RentUpdateRequest>{
    RentDTO updateActive(Long id);
    RentDTO updateFinish(Long id);
    RentDTO updatePayment(Long id);
    Map<String,Boolean> checkAvailability(Long id, RentAvailabilityRequest request);
}
