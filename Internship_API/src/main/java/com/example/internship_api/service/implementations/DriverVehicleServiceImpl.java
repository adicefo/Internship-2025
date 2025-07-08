package com.example.internship_api.service.implementations;

import com.example.internship_api.dto.*;
import com.example.internship_api.entity.Driver;
import com.example.internship_api.entity.DriverVehicle;
import com.example.internship_api.entity.Review;
import com.example.internship_api.entity.Vehicle;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.repository.DriverRepository;
import com.example.internship_api.repository.DriverVehicleRepository;
import com.example.internship_api.repository.VehicleRepository;
import com.example.internship_api.service.DriverVehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DriverVehicleServiceImpl extends BaseCRUDServiceImpl<DriverVehicleDTO, DriverVehicleSearchObject, DriverVehicle, DriverVehicleInsertRequest, DriverVehicleUpdateRequest> implements DriverVehicleService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    public DriverVehicleServiceImpl(DriverVehicleRepository repository, ModelMapper mapper) {
        super(repository,mapper,DriverVehicleDTO.class,DriverVehicle.class);
    }
    @Override
    public Map<String, Boolean> checkIfAssigned(Long driver_id) {
        var driver=driverRepository.findById(driver_id);
        if(!driver.isPresent())
            throw new EntityNotFoundException(driver_id,Driver.class);
       var instance= repository.findAll().stream()
               .filter(item->item.getDriver().getId()==driver_id)
               .filter(item->item.getDatePick().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
               .findAny();
       if(instance.isPresent())
              return Map.of("isAssigned",true);
         return Map.of("isAssigned",false);
    }

    @Override
    public DriverVehicleDTO updateFinish(DriverVehicleFinishRequest request) {
       var entity=repository.findAll()
               .stream()
               .filter(item->item.getDriver().getId()==request.getDriverId())
               .filter(item->item.getDatePick().toLocalDate().equals(request.getDatePick().toLocalDate()))
               .filter(item->item.getDateDrop()==null)
               .findFirst();
       if(!entity.isPresent())
           return null;
       var unwrapEntity=entity.get();
         unwrapEntity.setDateDrop(LocalDateTime.now());
         var vehicle=checkVehicle(unwrapEntity.getVehicle().getId());
            vehicle.setAvailable(true);
            vehicleRepository.save(vehicle);
            repository.save(unwrapEntity);
        return modelMapper.map(unwrapEntity,DriverVehicleDTO.class);
    }

    @Override
    protected void beforeInsert(DriverVehicleInsertRequest request, DriverVehicle entity) {
        var driver=checkDriver(request.getDriverId().longValue());
        var vehicle=checkVehicle(request.getVehicleId().longValue());

        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        entity.setDatePick(LocalDateTime.now());
        entity.setDriver(driver);
        entity.setVehicle(vehicle);

    }

    @Override
    protected void beforeUpdate(DriverVehicleUpdateRequest request, DriverVehicle driverVehicle) {

    }

    @Override
    protected void addFilter(DriverVehicleSearchObject search, List<DriverVehicle> query) {
        if (search==null) {
            return;
        }
        List<DriverVehicle> filteredQuery = query.stream()
                .filter(item->search.getDriverId()==null||item.getDriver().getId()==search.getDriverId())
                .filter(item->search.getVehicleId()==null||item.getVehicle().getId()==search.getVehicleId())
                .filter(item->search.getDatePick()==null||item.getDatePick().toLocalDate().equals(search.getDatePick().toLocalDate()))
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
    private Driver checkDriver(Long driver_id) {
        var driver=driverRepository.findById(driver_id);
        if(driver.isPresent())
            return driver.get();
        throw new EntityNotFoundException(driver_id,Driver.class);
    }
    private Vehicle checkVehicle(Long vehicle_id) {
        var vehicle=vehicleRepository.findById(vehicle_id);
        if(vehicle.isPresent())
            return vehicle.get();
        throw new EntityNotFoundException(vehicle_id,Vehicle.class);
    }
}
