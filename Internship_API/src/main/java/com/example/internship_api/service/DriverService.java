package com.example.internship_api.service;

import com.example.internship_api.data.model.ClientDTO;
import com.example.internship_api.dto.DriverDTO;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.dto.UserUpdateRequest;
import com.example.internship_api.data.search_object.ClientSearchObject;
import com.example.internship_api.dto.DriverSearchObject;

public interface DriverService extends BaseCRUDService<DriverDTO, DriverSearchObject,
        UserInsertRequest, UserUpdateRequest>{
    DriverDTO saveBasedOnUser(Long userId);
}
