package com.example.internship_api.service;

import com.example.internship_api.dto.AdminDTO;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.dto.UserUpdateRequest;
import com.example.internship_api.dto.AdminSearchObject;

public interface AdminService extends BaseCRUDService<AdminDTO, AdminSearchObject,
        UserInsertRequest, UserUpdateRequest>{
    AdminDTO saveBasedOnUser(Long userId);
}
