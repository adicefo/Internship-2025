package com.example.clientservice.service.implementation;

import com.example.clientservice.api.NotificationApiClient;
import com.example.clientservice.model.GetNotifications200Response;
import com.example.clientservice.model.NotificationDTO;
import com.example.clientservice.model.NotificationSearchObject;
import com.example.clientservice.model.NotificationUpsertRequest;
import com.example.clientservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationApiClient notificationApiClient;

    public NotificationServiceImpl(NotificationApiClient notificationApiClient) {
        this.notificationApiClient = notificationApiClient;
    }

    @Override
    public NotificationDTO deleteNotification(Integer id) {
        ResponseEntity<NotificationDTO> response=notificationApiClient.deleteNotification(id);
        return response.getBody();
    }

    @Override
    public NotificationDTO getNotificationById(Integer id) {
        ResponseEntity<NotificationDTO> response=notificationApiClient.getNotificationById(id);
        return response.getBody();

    }

    @Override
    public NotificationDTO createNotification(NotificationUpsertRequest request) {
        ResponseEntity<NotificationDTO>response=notificationApiClient.saveNotification(request);
        return response.getBody();
    }

    @Override
    public NotificationDTO updateNotification(Integer id, NotificationUpsertRequest request) {
        ResponseEntity<NotificationDTO>response=notificationApiClient.updateNotification(id, request);
        return response.getBody();
    }

    @Override
    public GetNotifications200Response getNotifications(NotificationSearchObject search) {
        ResponseEntity<GetNotifications200Response>response=notificationApiClient.getNotifications(search);
        return response.getBody();
    }
}
