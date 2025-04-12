package com.example.internship_api.service.implementations;

import com.example.internship_api.dto.*;
import com.example.internship_api.entity.Notification;
import com.example.internship_api.entity.Rent;
import com.example.internship_api.repository.NotificationRepository;
import com.example.internship_api.service.NotificationService;
import com.example.internship_api.service.RentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class NotificationServiceImpl extends BaseCRUDServiceImpl<NotificationDTO, NotificationSearchObject, Notification, NotificationUpsertRequest, NotificationUpsertRequest> implements NotificationService {

    public NotificationServiceImpl(NotificationRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, NotificationDTO.class, Notification.class);
    }
    @Override
    protected void beforeInsert(NotificationUpsertRequest request, Notification entity) {
        entity.setAddingDate(LocalDateTime.now());
        if(request.getImage() == null) {
            entity.setImage(null);
        }
    }

    @Override
    protected void beforeUpdate(NotificationUpsertRequest request, Notification entity) {

    }

    @Override
    protected void addFilter(NotificationSearchObject search, List<Notification> query) {
        if (search == null) {
            return;
        }
        List<Notification> filteredQuery = query.stream()
                .filter(item-> search.getTitle() == null || item.getTitle().startsWith(search.getTitle()))
                .filter(item -> search.getForClient() == null || item.getForClient()==search.getForClient())

                .collect(Collectors.toList());

        query.clear();
        query.addAll(filteredQuery);
    }
}
