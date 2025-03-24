package com.example.internship_api.service;

import com.example.internship_api.data.model.AdminDTO;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.request.UserUpdateRequest;
import com.example.internship_api.data.search_object.AdminSearchObject;
import com.example.internship_api.data.search_object.UserSearchObject;

public interface AdminService extends BaseCRUDService<AdminDTO, AdminSearchObject,
        UserInsertRequest, UserUpdateRequest>{
    AdminDTO saveBasedOnUser(Long userId);
}
