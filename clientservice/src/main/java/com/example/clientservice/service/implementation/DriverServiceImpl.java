package com.example.clientservice.service.implementation;

import com.example.clientservice.api.DriverApiClient;
import com.example.clientservice.model.DriverDTO;
import com.example.clientservice.model.DriverSearchObject;
import com.example.clientservice.model.GetDriver200Response;
import com.example.clientservice.model.UserInsertRequest;
import com.example.clientservice.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverApiClient driverApiClient;
    @Override
    public DriverDTO createDriver(UserInsertRequest request) {
       ResponseEntity<DriverDTO> response = driverApiClient.createDriver(request);
         return response.getBody();
    }

    @Override
    public DriverDTO createDriverBasedOnUser(Integer userId) {
        ResponseEntity<DriverDTO> response = driverApiClient.createDriverBasedOnUser(userId);
        return response.getBody();
    }

    @Override
    public DriverDTO deleteDriver(Integer id) {
       ResponseEntity<DriverDTO> response = driverApiClient.deleteDriver(id);
        return response.getBody();
    }

    @Override
    public DriverDTO getDriverById(Integer id) {
        ResponseEntity<DriverDTO> response = driverApiClient.getDriverById(id);
        return response.getBody();
    }

    @Override
    public GetDriver200Response getDriver(DriverSearchObject search) {
        ResponseEntity<GetDriver200Response> response = driverApiClient.getDriver(search);
        return response.getBody();
    }
}
