package com.example.internship_api.delegate;

import com.example.internship_api.api.RequestsApiDelegate;
import com.example.internship_api.dto.*;
import com.example.internship_api.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RequestDelegateImpl implements RequestsApiDelegate {
    @Autowired
    private RequestService service;

    @Override
    public ResponseEntity<GetRequests200Response> getRequests(RequestSearchObject search) {
        GetRequests200Response response = new GetRequests200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RequestDTO> getRequestById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RequestDTO> saveRequest(RequestInsertRequest requestInsertRequest) {
        return new ResponseEntity<>(service.save(requestInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RequestDTO> updateRequest(Integer id, RequestUpdateRequest requestUpdateRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), requestUpdateRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RequestDTO> deleteRequest(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
