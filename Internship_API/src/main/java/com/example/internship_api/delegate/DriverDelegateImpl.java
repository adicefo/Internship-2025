package com.example.internship_api.delegate;

import com.example.internship_api.api.DriverApiDelegate;
import com.example.internship_api.dto.DriverDTO;
import com.example.internship_api.dto.DriverSearchObject;
import com.example.internship_api.dto.GetDriver200Response;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DriverDelegateImpl implements DriverApiDelegate {

    @Autowired
    private DriverService service;

    @Override
    public ResponseEntity<GetDriver200Response> getDriver(DriverSearchObject search) {
        GetDriver200Response response = new GetDriver200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DriverDTO> createDriver(UserInsertRequest userInsertRequest) {
        return new ResponseEntity<>(service.save(userInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DriverDTO> createDriverBasedOnUser(Integer userId) {
        return new ResponseEntity<>(service.saveBasedOnUser(userId.longValue()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DriverDTO> getDriverById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DriverDTO> deleteDriver(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
