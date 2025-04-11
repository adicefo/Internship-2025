package com.example.internship_api.delegate;

import com.example.internship_api.api.CompanyPriceApiDelegate;
import com.example.internship_api.dto.CompanyPriceDTO;
import com.example.internship_api.dto.CompanyPriceInsertRequest;
import com.example.internship_api.dto.CompanyPriceSearchObject;
import com.example.internship_api.dto.GetCompanyPrices200Response;
import com.example.internship_api.service.CompanyPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompanyPriceDelegateImpl implements CompanyPriceApiDelegate {

    @Autowired
    private CompanyPriceService service;

    @Override
    public ResponseEntity<GetCompanyPrices200Response> getCompanyPrices(CompanyPriceSearchObject search) {
        GetCompanyPrices200Response response = new GetCompanyPrices200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CompanyPriceDTO> getCurrentCompanyPrice() {
        return new ResponseEntity<>(service.getCurrentPrice(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CompanyPriceDTO> saveCompanyPrice(CompanyPriceInsertRequest companyPriceInsertRequest) {
        return new ResponseEntity<>(service.save(companyPriceInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CompanyPriceDTO> getCompanyPriceById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CompanyPriceDTO> deleteCompanyPrice(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()),HttpStatus.OK);
    }
}
