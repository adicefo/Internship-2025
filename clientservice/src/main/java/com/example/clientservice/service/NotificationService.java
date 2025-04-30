package com.example.clientservice.service;

import com.example.clientservice.model.GetNotifications200Response;
import com.example.clientservice.model.NotificationDTO;
import com.example.clientservice.model.NotificationSearchObject;
import com.example.clientservice.model.NotificationUpsertRequest;

public interface NotificationService {
    NotificationDTO deleteNotification(Integer id);
    NotificationDTO getNotificationById(Integer id);
    NotificationDTO createNotification(NotificationUpsertRequest request);
    NotificationDTO updateNotification(Integer id, NotificationUpsertRequest request);
    GetNotifications200Response getNotifications(NotificationSearchObject search);
}
