package com.example.internship_api.delegate;

import com.example.internship_api.api.UsersApiDelegate;
import com.example.internship_api.dto.GetUsers200Response;
import com.example.internship_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserDelegateImpl implements UsersApiDelegate {
    @Autowired
    private  UserService service;

    @Override
    public ResponseEntity<GetUsers200Response> getUsers() {
        GetUsers200Response response = new GetUsers200Response();
        response.setItems(service.getAll(null).getResult());
        response.setCount(service.getAll(null).getCount());
        return ResponseEntity.ok(response);
    }
}
