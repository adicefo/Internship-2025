package com.example.internship_api.service.implementations;

import com.example.internship_api.dto.*;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public Map<String,Object> getDriversForReport(GeneralReportRequest request) {
        //takes all reviews for the given month and year
        var reviews = repository.findAll().stream()
                .filter(item -> item.getAddingDate() != null)
                .filter(item -> item.getAddingDate().getYear() == request.getYear())
                .filter(item -> item.getAddingDate().getMonthValue() == request.getMonth())
                .collect(Collectors.toList());

        //if it is empty returns null
        if (reviews.isEmpty()) {
            return Map.of("maxDriver", "null", "minDriver", "null");
        }

        //grouping driver and his average mark for that period
        var driverReviews = reviews.stream()
                .collect(Collectors.groupingBy(
                        x -> Map.of("name", x.getDriver().getUser().getName(), "surname", x.getDriver().getUser().getSurname()),
                        Collectors.averagingDouble(Review::getValue)
                ))
                .entrySet().stream()
                .map(entry -> Map.of(
                        "DriverName", entry.getKey(),
                        "AvgMark", Math.round(entry.getValue() * 100.0) / 100.0
                ))
                .collect(Collectors.toList());

        //finding driver with max and min average mark
        var maxAvgDriver = driverReviews.stream()
                .max(Comparator.comparingDouble(x -> (Double) x.get("AvgMark")))
                .orElse(Map.of("DriverName", "null", "AvgMark", 0.0));

        var minAvgDriver = driverReviews.stream()
                .min(Comparator.comparingDouble(x -> (Double) x.get("AvgMark")))
                .orElse(Map.of("DriverName", "null", "AvgMark", 0.0));

        return Map.of("maxDriver", maxAvgDriver, "minDriver", minAvgDriver);
    }
    @Override
    protected void beforeInsert(ReviewInsertRequest request, Review entity) {
        var route=routeRepository.findAll()
                .stream()
                .filter(
                        item->item.getClient().getId()==request.getReviewsId()&&item.getDriver().getId()==request.getReviewedId()
                ).findFirst();
        if(route.isPresent())
        {
            var unwrapRoute=route.get();
            entity.setRoute(unwrapRoute);
            var unwrapClient=checkClient(request.getReviewsId().longValue());
            var unwrapDriver=checkDriver(request.getReviewedId().longValue());
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
                .filter(item->search.getReviewsName()==null||item.getClient().getUser().getName().equals(search.getReviewsName()))
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