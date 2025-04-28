package com.example.clientservice.service;

import com.example.clientservice.model.*;

public interface AdminService {
    AdminDTO createAdmin(UserInsertRequest request);
    AdminDTO createAdminBasedOnUser(Integer userId);
    AdminDTO deleteAdmin(Integer id);
    GetAdmin200Response getAdmin(AdminSearchObject search);
    AdminDTO getAdminById(Integer id);
    AdminDTO updateAdmin(Integer id, UserUpdateRequest request);
}
