package com.example.internship_api.service;

import com.example.internship_api.dto.UserDTO;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.dto.UserSearchObject;
import com.example.internship_api.dto.UserUpdateRequest;

public interface UserService extends BaseCRUDService<UserDTO, UserSearchObject,
        UserInsertRequest, UserUpdateRequest> {

}
