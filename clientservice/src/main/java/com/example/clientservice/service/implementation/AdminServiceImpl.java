package com.example.clientservice.service.implementation;

import com.example.clientservice.api.AdminApiClient;
import com.example.clientservice.api.UsersApiClient;
import com.example.clientservice.model.*;
import com.example.clientservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminApiClient adminApiClient;

    @Override
    public AdminDTO createAdmin(UserInsertRequest request) {
        ResponseEntity<AdminDTO> response= adminApiClient.createAdmin(request);
        return response.getBody();
    }

    @Override
    public AdminDTO createAdminBasedOnUser(Integer userId) {
        ResponseEntity<AdminDTO>response= adminApiClient.createAdminBasedOnUser(userId);
        return response.getBody();
    }

    @Override
    public AdminDTO deleteAdmin(Integer id) {
        ResponseEntity<AdminDTO> response = adminApiClient.deleteAdmin(id);
        return response.getBody();
    }

    @Override
    public GetAdmin200Response getAdmin(AdminSearchObject search) {
        ResponseEntity<GetAdmin200Response> response = adminApiClient.getAdmin(search);
        return response.getBody();
    }

    @Override
    public AdminDTO getAdminById(Integer id) {
        ResponseEntity<AdminDTO> response = adminApiClient.getAdminById(id);
        return response.getBody();
    }

    @Override
    public AdminDTO updateAdmin(Integer id, UserUpdateRequest request) {
        ResponseEntity<AdminDTO> response = adminApiClient.updateAdmin(id, request);
        return response.getBody();
    }
}
