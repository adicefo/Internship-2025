package com.example.clientservice.controller;

import com.example.clientservice.model.GetNotifications200Response;
import com.example.clientservice.model.NotificationDTO;
import com.example.clientservice.model.NotificationSearchObject;
import com.example.clientservice.model.NotificationUpsertRequest;
import com.example.clientservice.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/get")
    public ResponseEntity<GetNotifications200Response> getNotifications(NotificationSearchObject search) {
        return new ResponseEntity<>(service.getNotifications(search), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getNotificationById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody @Valid NotificationUpsertRequest request) {
        return new ResponseEntity<>(service.createNotification(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(@PathVariable Integer id, @RequestBody @Valid NotificationUpsertRequest request) {
        return new ResponseEntity<>(service.updateNotification(id, request), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<NotificationDTO> deleteNotification(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteNotification(id), HttpStatus.OK);
    }

}
