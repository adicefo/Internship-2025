package com.example.internship_api.service.implementations;

import com.example.internship_api.dto.*;
import com.example.internship_api.entity.Client;
import com.example.internship_api.entity.Rent;
import com.example.internship_api.entity.Route;
import com.example.internship_api.entity.Vehicle;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.repository.ClientRepository;
import com.example.internship_api.repository.RentRepository;
import com.example.internship_api.repository.VehicleRepository;
import com.example.internship_api.service.RentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class RentServiceImpl extends BaseCRUDServiceImpl<RentDTO, RentSearchObject, Rent, RentInsertRequest, RentUpdateRequest> implements RentService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ClientRepository clientRepository;


    public RentServiceImpl(RentRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, RentDTO.class, Rent.class);
    }

    @Override
    public RentDTO updateActive(Long id) {
        var unwrapEntity=unwrapEntity(repository.findById(id),id);
        unwrapEntity.setStatus("active");
        repository.save(unwrapEntity);
        return modelMapper.map(unwrapEntity,modelClass);
    }

    @Override
    public RentDTO updateFinish(Long id) {
        var unwrapEntity=unwrapEntity(repository.findById(id),id);
        unwrapEntity.setStatus("finished");
        repository.save(unwrapEntity);
        return modelMapper.map(unwrapEntity,modelClass);
    }

    @Override
    public RentDTO updatePayment(Long id) {
        var unwrapEntity=unwrapEntity(repository.findById(id),id);
        unwrapEntity.setPaid(true);
        repository.save(unwrapEntity);
        return modelMapper.map(unwrapEntity,modelClass);
    }

    @Override
    public Map<String, Boolean> checkAvailability(Long id, RentAvailabilityRequest request) {
        var entity=unwrapEntity(repository.findById(id),id);
        var vehicle=checkVehicle(request.getVehicleId().longValue());
        if (
                request.getEndDate().isBefore(request.getRentDate()) ||
                        request.getEndDate().isEqual(request.getRentDate())
        )
            throw new IllegalArgumentException("Please input valid data");
        Optional<Rent> rent=repository.findAll().stream()
                .filter(item->item.getVehicle().getId()==vehicle.getId())
                .filter(item->item.getStatus().equals("active"))
                .filter(item->request.getRentDate().isBefore(item.getEndDate()))
                .filter(item->request.getEndDate().isAfter(item.getRentDate())).findAny();
        return Map.of("isAvailable",rent.isEmpty());
    }

    @Override
    protected void beforeInsert(RentInsertRequest request, Rent entity) {
        if (request.getRentDate()!=null&&request.getEndDate()!=null&&request.getEndDate().isBefore(request.getRentDate()))
            throw new IllegalArgumentException("End date must be greater than rent date");
        if(request.getRentDate()!=null&&request.getEndDate()!=null)
        {
            long numberOfDays = Duration.between(request.getRentDate(), request.getEndDate()).toDays();
            entity.setNumberOfDays((int) numberOfDays);

        }
        else
            entity.setNumberOfDays(0);
        var vehicle=checkVehicle(request.getVehicleId().longValue());
        var client=checkClient(request.getClientId().longValue());
        var fullPrice=entity.getNumberOfDays()*vehicle.getPrice();
        entity.setStatus("wait");
        entity.setFullPrice(fullPrice);
        entity.setVehicle(vehicle);
        entity.setClient(client);
    }

    @Override
    protected void beforeUpdate(RentUpdateRequest request, Rent entity) {
    if(request.getEndDate()!=null)
    {
        if(request.getEndDate().isBefore(entity.getRentDate()))
            throw new IllegalArgumentException("End date must be greater than rent date");
        if(entity.getRentDate()!=null&&entity.getEndDate()!=null)
        {
            long numberOfDays = Duration.between(entity.getRentDate(), request.getEndDate()).toDays();
            entity.setNumberOfDays((int) numberOfDays);

        }
        else
            entity.setNumberOfDays(0);
    }
        var vehicle=checkVehicle(request.getVehicleId().longValue());
        var fullPrice=entity.getNumberOfDays()*vehicle.getPrice();
        entity.setFullPrice(fullPrice);
        entity.setVehicle(vehicle);
    }

    @Override
    protected void addFilter(RentSearchObject search, List<Rent> query) {
        if (search == null) {
            return;
        }
        List<Rent> filteredQuery = query.stream()
                .filter(item-> search.getStatus() == null || item.getStatus().equals(search.getStatus()))
                .filter(item -> search.getClientId() == null || item.getClient().getId()==search.getClientId())
                .filter(item -> search.getVehicleId() == null || item.getVehicle().getId()==search.getVehicleId())
                .filter(item -> search.getUserId() == null || item.getClient().getUser().getId()==search.getUserId())
                .filter(item -> search.getRentDate() == null || item.getRentDate().equals(search.getRentDate()))
                .filter(item -> search.getEndDate() == null || item.getEndDate().equals(search.getEndDate()))
                .collect(Collectors.toList());
                if(search.getPageNumber()!=null&&search.getPageSize()!=null)
        {
        Pageable pageable=PageRequest.of(search.getPageNumber(),search.getPageSize());
        filteredQuery=repository.findAll(pageable).toList();
        }

        query.clear();
        query.addAll(filteredQuery);
    }

    private Vehicle checkVehicle(Long vehicle_id) {
        var vehicle=vehicleRepository.findById(vehicle_id);
        if(vehicle.isPresent())
            return vehicle.get();
        throw new EntityNotFoundException(vehicle_id,Vehicle.class);
    }
    private Client checkClient(Long client_id) {
        var client=clientRepository.findById(client_id);
        if(client.isPresent())
            return client.get();
        throw new EntityNotFoundException(client_id,Client.class);
    }
}
