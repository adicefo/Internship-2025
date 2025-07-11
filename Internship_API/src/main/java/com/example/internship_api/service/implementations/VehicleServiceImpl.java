package com.example.internship_api.service.implementations;

import com.example.internship_api.dto.*;
import com.example.internship_api.entity.Route;
import com.example.internship_api.entity.Vehicle;
import com.example.internship_api.repository.VehicleRepository;
import com.example.internship_api.service.RouteService;
import com.example.internship_api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl extends BaseCRUDServiceImpl<VehicleDTO, VehicleSearchObject, Vehicle, VehicleUpsertRequest, VehicleUpsertRequest> implements VehicleService {
    public VehicleServiceImpl(VehicleRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, VehicleDTO.class, Vehicle.class);
    }
    @Override
    protected void beforeInsert(VehicleUpsertRequest request, Vehicle entity) {
        if (request.getImage() == null) {
            entity.setImage(null);
        }
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
}
