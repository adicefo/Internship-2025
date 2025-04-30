package com.example.clientservice.controller;

import com.example.clientservice.model.*;
import com.example.clientservice.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService service;

    @PostMapping("/save")
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody @Valid UserInsertRequest userInsertRequest) {
        return new ResponseEntity<>(service.createAdmin(userInsertRequest), HttpStatus.CREATED);
    }
    @PostMapping("/saveBasedOnUser/{userId}")
    public ResponseEntity<AdminDTO> createAdminBasedOnUser(@PathVariable Integer userId) {
        return new ResponseEntity<>(service.createAdminBasedOnUser(userId), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AdminDTO> deleteAdmin(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteAdmin(id), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getAdminById(id), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<GetAdmin200Response> getAdmin(AdminSearchObject searchObject) {
        return new ResponseEntity<>(service.getAdmin(searchObject), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable Integer id, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return new ResponseEntity<>(service.updateAdmin(id, userUpdateRequest), HttpStatus.OK);
    }
}
