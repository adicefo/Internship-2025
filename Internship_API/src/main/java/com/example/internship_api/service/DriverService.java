package com.example.internship_api.service;

import com.example.internship_api.data.model.ClientDTO;
import com.example.internship_api.data.model.DriverDTO;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.request.UserUpdateRequest;
import com.example.internship_api.data.search_object.ClientSearchObject;
import com.example.internship_api.data.search_object.DriverSearchObject;

public interface DriverService extends BaseCRUDService<DriverDTO, DriverSearchObject,
        UserInsertRequest, UserUpdateRequest>{
    DriverDTO saveBasedOnUser(Long userId);
}
