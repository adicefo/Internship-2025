package com.example.clientservice.controller;

import com.example.clientservice.model.*;
import com.example.clientservice.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService service;

    @PostMapping("/save")
    public ResponseEntity<ClientDTO> createClient(@RequestBody @Valid UserInsertRequest userInsertRequest) {
        return new ResponseEntity<>(service.createClient(userInsertRequest), HttpStatus.CREATED);
    }
    @PostMapping("/saveBasedOnUser/{userId}")
    public ResponseEntity<ClientDTO> createClientBasedOnUser(@PathVariable Integer userId) {
        return new ResponseEntity<>(service.createClientBasedOnUser(userId), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ClientDTO> deleteClient(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteClient(id), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getClientById(id), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<GetClient200Response> getClient(@RequestParam(required = false) ClientSearchObject searchObject) {
        return new ResponseEntity<>(service.getClient(searchObject), HttpStatus.OK);
    }
}
