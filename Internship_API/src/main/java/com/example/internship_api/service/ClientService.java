package com.example.internship_api.service;

import com.example.internship_api.data.model.AdminDTO;
import com.example.internship_api.dto.ClientDTO;
import com.example.internship_api.data.model.DriverDTO;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.dto.UserUpdateRequest;
import com.example.internship_api.data.search_object.AdminSearchObject;
import com.example.internship_api.dto.ClientSearchObject;

public interface ClientService extends BaseCRUDService<ClientDTO, ClientSearchObject,
        UserInsertRequest, UserUpdateRequest>{
    ClientDTO saveBasedOnUser(Long userId);
}
