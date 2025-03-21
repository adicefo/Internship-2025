package com.example.internship_api.controller;

import com.example.internship_api.PagedResult;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.model.request.UserInsertRequest;
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
    public ResponseEntity<PagedResult<UserDTO>> getAll(@RequestParam(required = false) UserSearchObject searchObject) {

        return new ResponseEntity<>(userService.getAll(searchObject), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserInsertRequest request) {

        return new ResponseEntity<>(userService.save(request),HttpStatus.CREATED);
    }
}
