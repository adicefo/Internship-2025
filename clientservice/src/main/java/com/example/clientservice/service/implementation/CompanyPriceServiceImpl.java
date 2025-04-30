package com.example.clientservice.service.implementation;

import com.example.clientservice.api.CompanyPriceApiClient;
import com.example.clientservice.model.CompanyPriceDTO;
import com.example.clientservice.model.CompanyPriceInsertRequest;
import com.example.clientservice.model.CompanyPriceSearchObject;
import com.example.clientservice.model.GetCompanyPrices200Response;
import com.example.clientservice.service.CompanyPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompanyPriceServiceImpl implements CompanyPriceService {
    @Autowired
    private CompanyPriceApiClient apiClient;
    @Override
    public CompanyPriceDTO saveCompanyPrice(CompanyPriceInsertRequest request) {
        ResponseEntity<CompanyPriceDTO> response= apiClient.saveCompanyPrice(request);
        return response.getBody();
    }

    @Override
    public CompanyPriceDTO deleteCompanyPrice(Integer id) {
       ResponseEntity<CompanyPriceDTO> response= apiClient.deleteCompanyPrice(id);
        return response.getBody();
    }

    @Override
    public CompanyPriceDTO getCurrentCompanyPrice() {
        ResponseEntity<CompanyPriceDTO> response= apiClient.getCurrentCompanyPrice();
        return response.getBody();
    }

    @Override
    public GetCompanyPrices200Response getCompanyPrices(CompanyPriceSearchObject search) {
       ResponseEntity<GetCompanyPrices200Response> response= apiClient.getCompanyPrices(search);
       return response.getBody();
    }

    @Override
    public CompanyPriceDTO getCompanyPriceById(Integer id) {
        ResponseEntity<CompanyPriceDTO> response= apiClient.getCompanyPriceById(id);
        return response.getBody();
    }
}
