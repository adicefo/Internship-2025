package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.DriverDTO;
import com.example.internship_api.data.model.RouteDTO;
import com.example.internship_api.data.request.RouteInsertRequest;
import com.example.internship_api.data.request.RouteUpdateRequest;
import com.example.internship_api.data.search_object.RouteSearchObject;
import com.example.internship_api.entity.Client;
import com.example.internship_api.entity.CompanyPrice;
import com.example.internship_api.entity.Driver;
import com.example.internship_api.entity.Route;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.repository.ClientRepository;
import com.example.internship_api.repository.CompanyPriceRepository;
import com.example.internship_api.repository.DriverRepository;
import com.example.internship_api.repository.RouteRepository;
import com.example.internship_api.service.RouteService;
import com.example.internship_api.utils.DistanceUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl extends BaseCRUDServiceImpl<RouteDTO, RouteSearchObject, Route, RouteInsertRequest, RouteUpdateRequest> implements RouteService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CompanyPriceRepository companyPriceRepository;
    public RouteServiceImpl(RouteRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, RouteDTO.class, Route.class);
    }
    @Override
    public RouteDTO updateFinish(Long id) {
        var route=checkRoute(id);
        route.setStatus("finished");
        route.setEndDate(LocalDateTime.now());
        int duration=route.getEndDate().getMinute()-route.getStartDate().getMinute();
        route.setDuration(duration);
        repository.save(route);
        return modelMapper.map(route,RouteDTO.class);

    }

    @Override
    public RouteDTO updatePayment(Long id) {
        var route=checkRoute(id);
        route.setPaid(true);
        repository.save(route);
        return modelMapper.map(route,RouteDTO.class);
    }

    @Override
    protected void beforeInsert(RouteInsertRequest request, Route entity) {
        if(request.sourcePointLat()==null||request.sourcePointLon()==null||
                request.destinationPointLat()==null||request.destinationPointLon()==null){
            throw new IllegalArgumentException("Source and destination point must be provided");
        }
        var client=checkClient(request.client_id());
        var driver=checkDriver(request.driver_id());
        var distanceInMeters= DistanceUtils.getDistance(request.sourcePointLon(),request.sourcePointLat(),request.destinationPointLon(),request.destinationPointLat());
        var lastPrice=companyPriceRepository.findFirstByOrderByIdDesc();

        entity.setNumberOfKilometers(distanceInMeters/1000);
        entity.setFullPrice(entity.getNumberOfKilometers()*lastPrice.getPricePerKilometer());
        entity.setStatus("wait");
        entity.setClient(client);
        entity.setDriver(driver);
        entity.setCompanyPrice(lastPrice);

    }



    @Override
    protected void beforeUpdate(RouteUpdateRequest request, Route entity) {
      entity.setStartDate(LocalDateTime.now());
      entity.setStatus("active");
    }

    @Override
    protected void addFilter(RouteSearchObject search, List<Route> query) {
        if (search == null) {
            return;
        }
        List<Route> filteredQuery = query.stream()
                .filter(item-> search.getStatus() == null || item.getStatus().equals(search.getStatus()))
                .filter(item -> search.getClient_id() == null || item.getClient().getId()==search.getClient_id())
                .filter(item -> search.getDriver_id() == null || item.getDriver().getId()==search.getDriver_id())
                .filter(item -> search.getUser_id() == null || (item.getDriver().getUser().getId()==search.getUser_id()||
                        item.getClient().getUser().getId()==search.getUser_id()))
                .collect(Collectors.toList());

        query.clear();
        query.addAll(filteredQuery);
    }

    //method for checking if passed driver exist in db
    private Driver checkDriver(Long driver_id) {
        Optional<Driver> driver=driverRepository.findById(driver_id);
        if(driver.isPresent()){
            return driver.get();
        }
        throw new EntityNotFoundException(driver_id, Driver.class);
    }
    //method for checking if passed client exist in db
    private Client checkClient(Long client_id) {
        Optional<Client> client=clientRepository.findById(client_id);
        if(client.isPresent()){
            return client.get();
        }
        throw new EntityNotFoundException(client_id, Client.class);
    }
    //method for checking if passed route exist in db
    private Route checkRoute(Long id) {
        Optional<Route> route=repository.findById(id);
        if(route.isPresent()){
            return route.get();
        }
        throw new EntityNotFoundException(id, Route.class);
    }
}
