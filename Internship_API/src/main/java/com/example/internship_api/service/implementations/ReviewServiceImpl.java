package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.ReviewDTO;
import com.example.internship_api.data.request.ReviewInsertRequest;
import com.example.internship_api.data.request.ReviewUpdateRequest;
import com.example.internship_api.data.search_object.ReviewSearchObject;
import com.example.internship_api.entity.*;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.repository.ClientRepository;
import com.example.internship_api.repository.DriverRepository;
import com.example.internship_api.repository.ReviewRepository;
import com.example.internship_api.repository.RouteRepository;
import com.example.internship_api.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewServiceImpl extends BaseCRUDServiceImpl<ReviewDTO, ReviewSearchObject, Review, ReviewInsertRequest, ReviewUpdateRequest> implements ReviewService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RouteRepository routeRepository;

    public ReviewServiceImpl(ReviewRepository repository, ModelMapper modelMapper)
    {
        super(repository, modelMapper, ReviewDTO.class, Review.class);
    }
    @Override
    protected void beforeInsert(ReviewInsertRequest request, Review entity) {
        var route=routeRepository.findAll()
                .stream()
                .filter(
                        item->item.getClient().getId()==request.reviews_id()&&item.getDriver().getId()==request.reviewed_id()
                ).findFirst();
        if(route.isPresent())
        {
            var unwrapRoute=route.get();
            entity.setRoute(unwrapRoute);
            var unwrapClient=checkClient(request.reviews_id());
            var unwrapDriver=checkDriver(request.reviewed_id());
            entity.setAddingDate(LocalDateTime.now());
            entity.setClient(unwrapClient);
            entity.setDriver(unwrapDriver);
            entity.setRoute(unwrapRoute);
        }
        else
            throw new EntityNotFoundException(1000000L, Route.class);
    }

    @Override
    protected void beforeUpdate(ReviewUpdateRequest request, Review review) {

    }

    @Override
    protected void addFilter(ReviewSearchObject search, List<Review> query) {
        if (search == null) {
            return;
        }
        List<Review> filteredQuery = query.stream()
                .filter(item->search.getReviewedName()==null||item.getDriver().getUser().getName().equals(search.getReviewedName()))
                .filter(item->search.getReviewesName()==null||item.getClient().getUser().getName().equals(search.getReviewesName()))
                .collect(Collectors.toList());

        query.clear();
        query.addAll(filteredQuery);
    }
    private Client checkClient(Long client_id) {
        var client=clientRepository.findById(client_id);
        if(client.isPresent())
            return client.get();
        throw new EntityNotFoundException(client_id,Client.class);
    }
    private Driver checkDriver(Long driver_id) {
        var driver=driverRepository.findById(driver_id);
        if(driver.isPresent())
            return driver.get();
        throw new EntityNotFoundException(driver_id, Driver.class);
    }
}