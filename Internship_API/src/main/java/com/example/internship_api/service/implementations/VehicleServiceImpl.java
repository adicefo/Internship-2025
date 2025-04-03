package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.RouteDTO;
import com.example.internship_api.data.model.VehicleDTO;
import com.example.internship_api.data.request.RouteInsertRequest;
import com.example.internship_api.data.request.RouteUpdateRequest;
import com.example.internship_api.data.request.VehicleUpsertRequest;
import com.example.internship_api.data.search_object.RouteSearchObject;
import com.example.internship_api.data.search_object.VehicleSearchObject;
import com.example.internship_api.entity.Route;
import com.example.internship_api.entity.Vehicle;
import com.example.internship_api.repository.VehicleRepository;
import com.example.internship_api.service.RouteService;
import com.example.internship_api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl extends BaseCRUDServiceImpl<VehicleDTO, VehicleSearchObject, Vehicle, VehicleUpsertRequest, VehicleUpsertRequest> implements VehicleService {
    public VehicleServiceImpl(VehicleRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, VehicleDTO.class, Vehicle.class);
    }
    @Override
    protected void beforeInsert(VehicleUpsertRequest request, Vehicle vehicle) {
    if(request.name() == null)
        throw new IllegalArgumentException("Vehicle name cannot be null");

    if(request.price()<40||request.price()>65)
        throw new IllegalArgumentException("Price must be between 40 and 65");
    }

    @Override
    protected void beforeUpdate(VehicleUpsertRequest request, Vehicle vehicle) {

    }

    @Override
    protected void addFilter(VehicleSearchObject search, List<Vehicle> query) {
        if (search == null) {
            return;
        }
        List<Vehicle> filteredQuery = query.stream()
                .filter(item-> search.getAvailable() == null || item.getAvailable()==search.getAvailable())
                .filter(item -> search.getName() == null || item.getName().equals(search.getName()))
                .collect(Collectors.toList());

        query.clear();
        query.addAll(filteredQuery);
    }
}
