package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.AdminDTO;
import com.example.internship_api.data.model.ClientDTO;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.search_object.AdminSearchObject;
import com.example.internship_api.data.search_object.ClientSearchObject;
import com.example.internship_api.entity.Client;
import com.example.internship_api.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService service;

    @GetMapping("/get")
    public ResponseEntity<PagedResult<ClientDTO>> getAll(@ModelAttribute ClientSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<ClientDTO> save(@RequestBody UserInsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @PostMapping("/save/{userId}")
    public ResponseEntity<ClientDTO> save(@PathVariable Long userId) {

        return new ResponseEntity<>(service.saveBasedOnUser(userId),HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ClientDTO> delete(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
