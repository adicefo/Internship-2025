package com.example.internship_api.service;

import com.example.internship_api.data.model.NotificationDTO;
import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.request.NotificationUpsertRequest;
import com.example.internship_api.data.request.RentInsertRequest;
import com.example.internship_api.data.request.RentUpdateRequest;
import com.example.internship_api.data.search_object.NotificationSearchObject;
import com.example.internship_api.data.search_object.RentSearchObject;

public interface NotificationService extends BaseCRUDService<NotificationDTO, NotificationSearchObject,
        NotificationUpsertRequest, NotificationUpsertRequest>{

}
