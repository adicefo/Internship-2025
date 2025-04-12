package com.example.internship_api.delegate;

import com.example.internship_api.api.NotificationApiDelegate;
import com.example.internship_api.dto.GetNotifications200Response;
import com.example.internship_api.dto.NotificationDTO;
import com.example.internship_api.dto.NotificationSearchObject;
import com.example.internship_api.dto.NotificationUpsertRequest;
import com.example.internship_api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationDelegateImpl implements NotificationApiDelegate {
    @Autowired
    private NotificationService service;

    @Override
    public ResponseEntity<GetNotifications200Response> getNotifications(NotificationSearchObject search) {
        GetNotifications200Response response = new GetNotifications200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<NotificationDTO> getNotificationById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<NotificationDTO> saveNotification(NotificationUpsertRequest notificationUpsertRequest) {
        return new ResponseEntity<>(service.save(notificationUpsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<NotificationDTO> updateNotification(Integer id, NotificationUpsertRequest notificationUpsertRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), notificationUpsertRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NotificationDTO> deleteNotification(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
