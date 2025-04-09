package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.request.UserUpdateRequest;
import com.example.internship_api.data.search_object.UserSearchObject;
import com.example.internship_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/user")
//public class UserController {
//    @Autowired
//    private UserService service;
//
//    @GetMapping("/get")
//    public ResponseEntity<PagedResult<UserDTO>> getAll(@ModelAttribute UserSearchObject searchObject) {
//
//        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
//    }
//
//    @GetMapping("/getById/{id}")
//    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
//
//        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserInsertRequest request) {
//
//        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
//    }
//    @PutMapping("/update/{id}")
//    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,@Valid @RequestBody UserUpdateRequest request) {
//
//        return new ResponseEntity<>(service.updateById(id,request),HttpStatus.OK);
//    }
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
//        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
//    }
//
//
//}
