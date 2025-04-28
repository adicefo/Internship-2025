package com.example.clientservice.service;

import com.example.clientservice.model.DriverDTO;
import com.example.clientservice.model.DriverSearchObject;
import com.example.clientservice.model.GetDriver200Response;
import com.example.clientservice.model.UserInsertRequest;

public interface DriverService {
    DriverDTO createDriver(UserInsertRequest request);
    DriverDTO createDriverBasedOnUser(Integer userId);
    DriverDTO deleteDriver(Integer id);
    DriverDTO getDriverById(Integer id);
    GetDriver200Response getDriver(DriverSearchObject search);
}
