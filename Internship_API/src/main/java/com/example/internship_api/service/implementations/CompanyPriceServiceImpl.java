package com.example.internship_api.service.implementations;

import com.example.internship_api.dto.CompanyPriceDTO;
import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.dto.CompanyPriceInsertRequest;
import com.example.internship_api.dto.CompanyPriceUpdateRequest;
import com.example.internship_api.data.request.RentInsertRequest;
import com.example.internship_api.data.request.RentUpdateRequest;
import com.example.internship_api.dto.CompanyPriceSearchObject;
import com.example.internship_api.data.search_object.RentSearchObject;
import com.example.internship_api.entity.CompanyPrice;
import com.example.internship_api.entity.Rent;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.repository.CompanyPriceRepository;
import com.example.internship_api.repository.RentRepository;
import com.example.internship_api.service.CompanyPriceService;
import com.example.internship_api.service.RentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                .filter(item->search.getPricePerKilometer()==null|| search.getPricePerKilometer().equals(item.getPricePerKilometer()))

                .collect(Collectors.toList());
        if(search.getPageNumber()!=null&&search.getPageSize()!=null)
        {
        Pageable pageable=PageRequest.of(search.getPageNumber(),search.getPageSize());
        filteredQuery=repository.findAll(pageable).toList();
        }
        query.clear();
        query.addAll(filteredQuery);
    }
}
