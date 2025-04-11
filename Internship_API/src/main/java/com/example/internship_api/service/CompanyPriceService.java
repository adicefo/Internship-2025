package com.example.internship_api.service;

import com.example.internship_api.dto.CompanyPriceDTO;
import com.example.internship_api.dto.CompanyPriceInsertRequest;
import com.example.internship_api.dto.CompanyPriceUpdateRequest;
import com.example.internship_api.dto.CompanyPriceSearchObject;

public interface CompanyPriceService extends BaseCRUDService<CompanyPriceDTO, CompanyPriceSearchObject,
        CompanyPriceInsertRequest, CompanyPriceUpdateRequest>{
    CompanyPriceDTO getCurrentPrice();
}
