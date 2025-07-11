package com.example.clientservice.service;

import com.example.clientservice.model.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface UserService {
UserDTO createUser( UserInsertRequest request);
UserDTO deleteUser(Integer id);
UserDTO getUserById(Integer id);
ResponseEntity<GetUsers200Response> getUsers(UserSearchObject searchObject);
UserDTO updateUser(Integer id, UserUpdateRequest request);

}
