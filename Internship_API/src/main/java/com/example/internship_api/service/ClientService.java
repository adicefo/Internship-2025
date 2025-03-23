package com.example.internship_api.service;

import com.example.internship_api.data.model.AdminDTO;
import com.example.internship_api.data.model.ClientDTO;
import com.example.internship_api.data.model.DriverDTO;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.request.UserUpdateRequest;
import com.example.internship_api.data.search_object.AdminSearchObject;
import com.example.internship_api.data.search_object.ClientSearchObject;

public interface ClientService extends BaseCRUDService<ClientDTO, ClientSearchObject,
        UserInsertRequest, UserUpdateRequest>{
    ClientDTO saveBasedOnUser(Long userId);
}
