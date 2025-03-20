package com.example.internship_api.service;

import com.example.internship_api.data.model.User;
import com.example.internship_api.data.model.request.UserInsertRequest;
import com.example.internship_api.data.model.request.UserUpdateRequest;
import com.example.internship_api.data.model.search_object.UserSearchObject;

public interface UserService extends BaseCRUDService<User, UserSearchObject,
        UserInsertRequest, UserUpdateRequest> {

}
