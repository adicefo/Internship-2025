package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.RequestDTO;
import com.example.internship_api.data.model.StatisticsDTO;
import com.example.internship_api.data.request.RequestInsertRequest;
import com.example.internship_api.data.request.RequestUpdateRequest;
import com.example.internship_api.data.request.StatisticsInsertRequest;
import com.example.internship_api.data.request.StatisticsUpdateRequest;
import com.example.internship_api.data.search_object.RequestSearchObject;
import com.example.internship_api.data.search_object.StatisticsSearchObject;
import com.example.internship_api.entity.Driver;
import com.example.internship_api.entity.Request;
import com.example.internship_api.entity.Route;
import com.example.internship_api.entity.Statistics;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.repository.DriverRepository;
import com.example.internship_api.repository.RequestRepository;
import com.example.internship_api.repository.RouteRepository;
import com.example.internship_api.service.RequestService;
import com.example.internship_api.service.StatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class RequestServiceImpl extends BaseCRUDServiceImpl<RequestDTO, RequestSearchObject, Request, RequestInsertRequest, RequestUpdateRequest> implements RequestService {

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private DriverRepository driverRepository;

    public RequestServiceImpl(RequestRepository repository, ModelMapper mapper) {
        super(repository, mapper, RequestDTO.class, Request.class);
    }

    @Override
    protected void beforeInsert(RequestInsertRequest request, Request entity) {
        var route=checkRoute(request.route_id());
        var driver=checkDriver(request.driver_id());
        entity.setRoute(route);
        entity.setDriver(driver);
    }

    @Override
    protected void beforeUpdate(RequestUpdateRequest request, Request entity) {
        var route=checkRoute(entity.getRoute().getId());
        if(entity.getAccepted())
            route.setStatus("active");
        else
            route.setStatus("finished");

    }

    @Override
    protected void addFilter(RequestSearchObject search, List<Request> query) {
        if (search == null) {
            return;
        }
        List<Request> filteredQuery = query.stream()
                .filter(item -> search.getDriver_id() == null || item.getDriver().getId()==search.getDriver_id())
                .filter(item -> search.getRoute_id() == null || item.getRoute().getId()==search.getRoute_id())
                        .collect(Collectors.toList());
        if(search.getAccepted()==null&&search.getRoute_id()==null&&search.getDriver_id()!=null)
            filteredQuery=query.stream()
                    .filter(item -> item.getDriver().getId()==search.getDriver_id()&&item.getAccepted()==null)
                    .collect(Collectors.toList());


        query.clear();
        query.addAll(filteredQuery);
    }
    private Driver checkDriver(Long driver_id) {
        var driver=driverRepository.findById(driver_id);
        if(driver.isPresent())
            return driver.get();
        throw new EntityNotFoundException(driver_id, Driver.class);
    }
    private Route checkRoute(Long route_id) {
        var route=routeRepository.findById(route_id);
        if(route.isPresent())
            return route.get();
        throw new EntityNotFoundException(route_id, Driver.class);
    }
}
