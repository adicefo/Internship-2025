package com.example.internship_api.service.implementations;

import com.example.internship_api.dto.*;
import com.example.internship_api.entity.Driver;
import com.example.internship_api.entity.Review;
import com.example.internship_api.entity.Statistics;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.repository.DriverRepository;
import com.example.internship_api.repository.RouteRepository;
import com.example.internship_api.repository.StatisticsRepository;
import com.example.internship_api.service.StatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl extends BaseCRUDServiceImpl<StatisticsDTO, StatisticsSearchObject, Statistics, StatisticsInsertRequest, StatisticsUpdateRequest> implements StatisticsService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RouteRepository routeRepository;

    public StatisticsServiceImpl(StatisticsRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, StatisticsDTO.class, Statistics.class);
    }


    @Override
    public StatisticsDTO updateFinish(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Statistics.class));

        entity.setEndOfWork(LocalDateTime.now());

        // Update for driver
        var driver =checkDriver(entity.getDriver().getId());

        var drives = routeRepository.findAll().stream()
                .filter(r -> r.getDriver().getId()==entity.getDriver().getId())
                .filter(r -> r.getStatus().equals("finished"))
                .filter(r -> r.getStartDate().toLocalDate().equals(entity.getBeginningOfWork().toLocalDate()))
                .collect(Collectors.toList());

        driver.setNumberOfClientsAmount(driver.getNumberOfClientsAmount() + drives.size());
        driver.setNumberOfHoursAmount(driver.getNumberOfHoursAmount() +
                Duration.between(entity.getBeginningOfWork(), entity.getEndOfWork()).toHoursPart());

        repository.save(entity);
        driverRepository.save(driver);

        return modelMapper.map(entity, StatisticsDTO.class);
    }

    @Override
    protected void beforeInsert(StatisticsInsertRequest request, Statistics entity) {
        var driver=checkDriver(request.getDriverId().longValue());
        entity.setBeginningOfWork(LocalDateTime.now());
        entity.setNumberOfClients(0);
        entity.setNumberOfHours(0);
        entity.setPriceAmount(0.0);
        entity.setDriver(driver);
    }

    @Override
    protected void beforeUpdate(StatisticsUpdateRequest request, Statistics entity) {
        entity.setNumberOfHours((int) Duration.between(entity.getBeginningOfWork(), LocalDateTime.now()).toHours());
        var drivesOnSentDay=routeRepository.findAll()
                .stream()
                .filter(item->item.getDriver().getId()==entity.getDriver().getId())
                .filter(item->item.getStatus().equals("finished"))
                .filter(item->item.getStartDate().toLocalDate().equals(entity.getBeginningOfWork().toLocalDate()))
                .collect(Collectors.toList());
        entity.setNumberOfClients(drivesOnSentDay.size());
        entity.setPriceAmount(drivesOnSentDay.stream().mapToDouble(item->item.getFullPrice()).sum());
    }

    @Override
    protected void addFilter(StatisticsSearchObject search, List<Statistics> query) {
        if (search == null) {
            return;
        }
        List<Statistics> filteredQuery = query.stream()
                .filter(item->search.getDriverId()==null||item.getDriver().getId()==search.getDriverId())
                .filter(item->search.getBeginningOfWork()==null||item.getBeginningOfWork().equals(search.getBeginningOfWork()))
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
}
