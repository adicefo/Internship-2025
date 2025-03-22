package com.example.internship_api.controller;

import com.example.internship_api.PagedResult;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.model.request.UserInsertRequest;
import com.example.internship_api.data.model.request.UserUpdateRequest;
import com.example.internship_api.data.model.search_object.UserSearchObject;
import com.example.internship_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<PagedResult<UserDTO>> getAll(@ModelAttribute UserSearchObject searchObject) {

        return new ResponseEntity<>(userService.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(userService.getById(id),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserInsertRequest request) {

        return new ResponseEntity<>(userService.save(request),HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {

        return new ResponseEntity<>(userService.updateById(id,request),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteById(id),HttpStatus.OK);
    }


}
