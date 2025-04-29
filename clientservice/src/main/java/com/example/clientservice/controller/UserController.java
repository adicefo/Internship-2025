package com.example.clientservice.controller;

import com.example.clientservice.model.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.clientservice.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserInsertRequest userInsertRequest) {
        return new ResponseEntity<>(service.createUser(userInsertRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Integer id) {
       return new ResponseEntity<>(service.deleteUser(id), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
      return new ResponseEntity<>(service.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<GetUsers200Response> getUsers(@ModelAttribute UserSearchObject searchObject) {
       return new ResponseEntity<>(service.getUsers(searchObject), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
       return new ResponseEntity<>(service.updateUser(id, userUpdateRequest), HttpStatus.OK);
    }
}
