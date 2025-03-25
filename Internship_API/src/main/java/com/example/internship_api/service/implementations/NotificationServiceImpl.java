package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.NotificationDTO;
import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.request.NotificationUpsertRequest;
import com.example.internship_api.data.request.RentInsertRequest;
import com.example.internship_api.data.request.RentUpdateRequest;
import com.example.internship_api.data.search_object.NotificationSearchObject;
import com.example.internship_api.data.search_object.RentSearchObject;
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
                .filter(item -> search.getForClient() == null || item.isForClient()==search.getForClient())

                .collect(Collectors.toList());

        query.clear();
        query.addAll(filteredQuery);
    }
}
