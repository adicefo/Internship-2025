package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.request.RentAvailabilityRequest;
import com.example.internship_api.data.request.RentInsertRequest;
import com.example.internship_api.data.request.RentUpdateRequest;
import com.example.internship_api.data.search_object.RentSearchObject;
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
        var vehicle=checkVehicle(request.vehicle_id());
        if(request.endDate().isBefore(request.rentDate())||request.endDate().isEqual(request.rentDate())
        ||!request.rentDate().isEqual(entity.getRentDate())||!request.endDate().isEqual(entity.getEndDate()))
            throw new IllegalArgumentException("Please input valid data");
        Optional<Rent> rent=repository.findAll().stream()
                .filter(item->item.getVehicle().getId()==vehicle.getId())
                .filter(item->item.getStatus().equals("active"))
                .filter(item->request.rentDate().isBefore(item.getEndDate()))
                .filter(item->request.endDate().isAfter(item.getRentDate())).findAny();
        return Map.of("isAvailable",!rent.isPresent());
    }

    @Override
    protected void beforeInsert(RentInsertRequest request, Rent entity) {
        if (request.rentDate()!=null&&request.endDate()!=null&&request.endDate().isBefore(request.rentDate()))
            throw new IllegalArgumentException("End date must be greater than rent date");
        if(request.rentDate()!=null&&request.endDate()!=null)
        {
            long numberOfDays = Duration.between(request.rentDate(), request.endDate()).toDays();
            entity.setNumberOfDays((int) numberOfDays);

        }
        else
            entity.setNumberOfDays(0);
        var vehicle=checkVehicle(request.vehicle_id());
        var client=checkClient(request.client_id());
        var fullPrice=entity.getNumberOfDays()*vehicle.getPrice();
        entity.setStatus("wait");
        entity.setFullPrice(fullPrice);
        entity.setVehicle(vehicle);
        entity.setClient(client);
    }

    @Override
    protected void beforeUpdate(RentUpdateRequest request, Rent entity) {
    if(request.endDate()!=null)
    {
        if(request.endDate().isBefore(entity.getRentDate()))
            throw new IllegalArgumentException("End date must be greater than rent date");
        if(entity.getRentDate()!=null&&entity.getEndDate()!=null)
        {
            long numberOfDays = Duration.between(entity.getRentDate(), request.endDate()).toDays();
            entity.setNumberOfDays((int) numberOfDays);

        }
        else
            entity.setNumberOfDays(0);
    }
        var vehicle=checkVehicle(request.vehicle_id());
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
                .filter(item -> search.getClient_id() == null || item.getClient().getId()==search.getClient_id())
                .filter(item -> search.getVehicle_id() == null || item.getVehicle().getId()==search.getVehicle_id())
                .filter(item -> search.getUser_id() == null || item.getClient().getUser().getId()==search.getUser_id())
                .filter(item -> search.getRentDate() == null || item.getRentDate().equals(search.getRentDate()))
                .filter(item -> search.getEndDate() == null || item.getEndDate().equals(search.getEndDate()))
                .collect(Collectors.toList());

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
