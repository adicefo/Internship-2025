package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.CompanyPriceDTO;
import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.request.CompanyPriceInsertRequest;
import com.example.internship_api.data.request.CompanyPriceUpdateRequest;
import com.example.internship_api.data.request.RentInsertRequest;
import com.example.internship_api.data.request.RentUpdateRequest;
import com.example.internship_api.data.search_object.CompanyPriceSearchObject;
import com.example.internship_api.data.search_object.RentSearchObject;
import com.example.internship_api.entity.CompanyPrice;
import com.example.internship_api.entity.Rent;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.repository.CompanyPriceRepository;
import com.example.internship_api.repository.RentRepository;
import com.example.internship_api.service.CompanyPriceService;
import com.example.internship_api.service.RentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CompanyPriceServiceImpl extends BaseCRUDServiceImpl<CompanyPriceDTO, CompanyPriceSearchObject, CompanyPrice, CompanyPriceInsertRequest, CompanyPriceUpdateRequest> implements CompanyPriceService {

    public CompanyPriceServiceImpl(CompanyPriceRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, CompanyPriceDTO.class, CompanyPrice.class);
    }
    @Override
    public CompanyPriceDTO getCurrentPrice() {
        var item = ((CompanyPriceRepository) repository).findFirstByOrderByIdDesc();
        if (item == null) {
            throw new EntityNotFoundException(1L,CompanyPrice.class);
        }
        return modelMapper.map(item, CompanyPriceDTO.class);
    }

    @Override
    protected void beforeInsert(CompanyPriceInsertRequest request, CompanyPrice entity) {
        if(request.pricePerKilometer()<1||request.pricePerKilometer()>15)
            throw new IllegalArgumentException("Price per kilometer must be between 1 and 15");
        entity.setAddingDate(LocalDateTime.now());
    }

    @Override
    protected void beforeUpdate(CompanyPriceUpdateRequest request, CompanyPrice companyPrice) {

    }

    @Override
    protected void addFilter(CompanyPriceSearchObject search, List<CompanyPrice> query) {
        if(search==null)
            return;

        List<CompanyPrice> filteredQuery=query.stream()
                .filter(item->search.getPricePerKilometer()==null|| search.getPricePerKilometer()==item.getPricePerKilometer())
                .filter(item->search.getAddingDateGTE()==null||item.getAddingDate().isAfter(search.getAddingDateGTE()))
                .filter(item->search.getAddingDateLTE()==null||item.getAddingDate().isBefore(search.getAddingDateLTE()))
                .collect(Collectors.toList());
        query.clear();
        query.addAll(filteredQuery);
    }
}
