package com.example.clientservice.service.implementation;

import com.example.clientservice.customClients.CustomUserApiClient;
import com.example.clientservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.clientservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private CustomUserApiClient usersApiClient;


    @Override
    public UserDTO createUser(UserInsertRequest request) {
       ResponseEntity<UserDTO>response= usersApiClient.createUser(request);
       return response.getBody();
    }

    @Override
    public UserDTO deleteUser(Integer id) {
        ResponseEntity<UserDTO> response = usersApiClient.deleteUser(id);
        return response.getBody();
    }

    @Override
    public UserDTO getUserById(Integer id) {
        ResponseEntity<UserDTO> response = usersApiClient.getUserById(id);
        return response.getBody();
    }

    @Override
    public ResponseEntity<GetUsers200Response> getUsers(UserSearchObject searchObject) {
      return usersApiClient.getUsers(searchObject);
    }

    @Override
    public UserDTO updateUser(Integer id, UserUpdateRequest request) {
        ResponseEntity<UserDTO> response = usersApiClient.updateUser(id, request);
        return response.getBody();
    }
}
