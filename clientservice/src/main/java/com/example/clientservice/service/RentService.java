package com.example.clientservice.service;

import com.example.clientservice.model.*;

import java.util.Map;

public interface RentService {
    RentDTO createRent(RentInsertRequest rentInsertRequest);

    RentDTO deleteRent(Integer id);

    RentDTO getRentById(Integer id);

    GetRents200Response getRent(RentSearchObject searchObject);

    RentDTO updateRent(Integer id, RentUpdateRequest rentUpdateRequest);

    Map<String,Boolean> checkRentAvailability(Integer id, RentAvailabilityRequest request);

    RentDTO updateRentActive(Integer id);
    RentDTO updateRentFinish(Integer id);
    RentDTO updateRentPayment(Integer id);
}
