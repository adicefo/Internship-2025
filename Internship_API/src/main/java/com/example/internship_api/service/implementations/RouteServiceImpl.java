package com.example.internship_api.service.implementations;

import com.example.internship_api.dto.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        var route=unwrapEntity( repository.findById(id),id);
        route.setStatus("finished");
        route.setEndDate(LocalDateTime.now());
        long duration= Duration.between(route.getStartDate(), route.getEndDate()).toMinutes();
        route.setDuration((int)duration);
        repository.save(route);
        return modelMapper.map(route,RouteDTO.class);

    }

    @Override
    public RouteDTO updatePayment(Long id) {
        var route=unwrapEntity( repository.findById(id),id);
        route.setPaid(true);
        repository.save(route);
        return modelMapper.map(route,RouteDTO.class);
    }

    @Override
    public Map<String, Double> getAmountForReport(GeneralReportRequest request) {
        var routes=repository.findAll()
                .stream()
                .filter(item->item.getStatus().equals("finished"))
                .filter(item->item.getEndDate()!=null)
                .filter(item->item.getEndDate().getMonthValue()==request.getMonth())
                .filter(item->item.getEndDate().getYear()==request.getYear())
                .collect(Collectors.toList());
        var amount=routes.stream().mapToDouble(item->item.getFullPrice()).sum();
        if(amount==0)
            return Map.of("fullAmount",0.0);
        return Map.of("fullAmount",amount);
    }
    @Override
public List<RouteClientCountDTO> getTopClientsByRoute() {
    var routes = repository.findAll()
        .stream()
        .filter(route -> route.getStartDate() != null)
        .filter(route -> "finished".equals(route.getStatus()))
        .filter(route -> route.getClient() != null)
        .collect(Collectors.toList());

    Map<String, Long> routeCountByClient = routes.stream()
        .collect(Collectors.groupingBy(
            route -> route.getClient().getUser().getName() + " " + route.getClient().getUser().getSurname(),
            Collectors.counting()
        ));

    return routeCountByClient.entrySet()
        .stream()
        .map(entry -> new RouteClientCountDTO()
            .client(entry.getKey())
            .count(entry.getValue().intValue())) // Convert Long to Integer
        .sorted(Comparator.comparingInt(RouteClientCountDTO::getCount).reversed())
        .collect(Collectors.toList());
}


    @Override
    protected void beforeInsert(RouteInsertRequest request, Route entity) {
        if(request.getSourcePointLat()==null||request.getSourcePointLon()==null||
                request.getDestinationPointLat()==null||request.getDestinationPointLon()==null){
            throw new IllegalArgumentException("Source and destination point must be provided");
        }
        var client=checkClient(request.getClientId().longValue());
        var driver=checkDriver(request.getDriverId().longValue());
        var distanceInMeters= DistanceUtils.getDistance(request.getSourcePointLon(),request.getSourcePointLat(),request.getDestinationPointLon(),request.getDestinationPointLat());
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
                .filter(item -> search.getClientId() == null || item.getClient().getId()==search.getClientId())
                .filter(item -> search.getDriverId() == null || item.getDriver().getId()==search.getDriverId())
                .filter(item -> search.getUserId() == null || (item.getDriver().getUser().getId()==search.getUserId()||
                        item.getClient().getUser().getId()==search.getUserId()))
                .collect(Collectors.toList());
    if (search.getPageNumber() != null && search.getPageSize() != null) {
        int start = search.getPageNumber() * search.getPageSize();
        int end = Math.min(start + search.getPageSize(), filteredQuery.size());

        if (start < end) {
            filteredQuery = filteredQuery.subList(start, end);
        } else {
            filteredQuery = new ArrayList<>();
        }
    }

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
   

}
