package com.example.internship_api.delegate;

import com.example.internship_api.api.RentApiDelegate;
import com.example.internship_api.dto.*;
import com.example.internship_api.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RentDelegateImpl implements RentApiDelegate {

    @Autowired
    private RentService service;

    @Override
    public ResponseEntity<GetRents200Response> getRents(RentSearchObject search) {
        GetRents200Response response = new GetRents200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RentDTO> getRentById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RentDTO> saveRent(RentInsertRequest rentInsertRequest) {
        return new ResponseEntity<>(service.save(rentInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> checkRentAvailability(Integer id, RentAvailabilityRequest rentAvailabilityRequest) {
        return new ResponseEntity<>(service.checkAvailability(id.longValue(), rentAvailabilityRequest), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<Map<String, Double>> getAmountForReportRent(GeneralReportRequest generalReportRequest) {
        return new ResponseEntity<>(service.getAmountForReport(generalReportRequest), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<RentDTO> updateRent(Integer id, RentUpdateRequest rentUpdateRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), rentUpdateRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RentDTO> updateRentActive(Integer id) {
        return new ResponseEntity<>(service.updateActive(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RentDTO> updateRentFinish(Integer id) {
        return new ResponseEntity<>(service.updateFinish(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RentDTO> updateRentPayment(Integer id) {
        return new ResponseEntity<>(service.updatePayment(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RentDTO> deleteRent(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
