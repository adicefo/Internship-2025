package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.AdminDTO;
import com.example.internship_api.data.model.DriverDTO;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.request.UserUpdateRequest;
import com.example.internship_api.data.search_object.AdminSearchObject;
import com.example.internship_api.data.search_object.UserSearchObject;
import com.example.internship_api.service.AdminService;
import com.example.internship_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService service;

    @GetMapping("/get")
    public ResponseEntity<PagedResult<AdminDTO>> getAll(@ModelAttribute AdminSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AdminDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<AdminDTO> save(@RequestBody UserInsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @PostMapping("/save/{userId}")
    public ResponseEntity<AdminDTO> save(@PathVariable Long userId) {

        return new ResponseEntity<>(service.saveBasedOnUser(userId),HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<AdminDTO> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {

        return new ResponseEntity<>(service.updateById(id,request),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AdminDTO> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
