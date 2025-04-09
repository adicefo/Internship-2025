package com.example.internship_api.service;

import com.example.internship_api.dto.UserDTO;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.request.UserUpdateRequest;
import com.example.internship_api.dto.UserSearchObject;

public interface UserService extends BaseCRUDService<UserDTO, UserSearchObject,
        UserInsertRequest, UserUpdateRequest> {

}
