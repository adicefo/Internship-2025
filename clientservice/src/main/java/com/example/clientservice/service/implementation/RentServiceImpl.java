package com.example.clientservice.service.implementation;

import com.example.clientservice.api.RentApiClient;
import com.example.clientservice.model.*;
import com.example.clientservice.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RentServiceImpl implements RentService {

    @Autowired
    private RentApiClient rentApiClient;
    @Override
    public RentDTO createRent(RentInsertRequest rentInsertRequest) {
        ResponseEntity<RentDTO> response= rentApiClient.saveRent(rentInsertRequest);
        return response.getBody();
    }

    @Override
    public RentDTO deleteRent(Integer id) {
       ResponseEntity<RentDTO> response= rentApiClient.deleteRent(id);
        return response.getBody();
    }

    @Override
    public RentDTO getRentById(Integer id) {
        ResponseEntity<RentDTO> response= rentApiClient.getRentById(id);
        return response.getBody();
    }

    @Override
    public GetRents200Response getRent(RentSearchObject searchObject) {
        ResponseEntity<GetRents200Response> response= rentApiClient.getRents(searchObject);
        return response.getBody();
    }

    @Override
    public RentDTO updateRent(Integer id, RentUpdateRequest rentUpdateRequest) {
        ResponseEntity<RentDTO> response= rentApiClient.updateRent(id,rentUpdateRequest);
        return response.getBody();
    }

    @Override
    public Map<String, Boolean> checkRentAvailability(Integer id, RentAvailabilityRequest request) {
        ResponseEntity<Map<String, Boolean>> response= rentApiClient.checkRentAvailability(id, request);
        return response.getBody();
    }

    @Override
    public RentDTO updateRentActive(Integer id) {
       ResponseEntity<RentDTO> response= rentApiClient.updateRentActive(id);
        return response.getBody();
    }

    @Override
    public RentDTO updateRentFinish(Integer id) {
        ResponseEntity<RentDTO> response= rentApiClient.updateRentFinish(id);
        return response.getBody();
    }

    @Override
    public RentDTO updateRentPayment(Integer id) {
        ResponseEntity<RentDTO> response= rentApiClient.updateRentPayment(id);
        return response.getBody();
    }
}
