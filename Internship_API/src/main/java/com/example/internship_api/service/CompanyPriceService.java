package com.example.internship_api.service;

import com.example.internship_api.data.model.CompanyPriceDTO;
import com.example.internship_api.data.request.CompanyPriceInsertRequest;
import com.example.internship_api.data.request.CompanyPriceUpdateRequest;
import com.example.internship_api.data.search_object.CompanyPriceSearchObject;

public interface CompanyPriceService extends BaseCRUDService<CompanyPriceDTO, CompanyPriceSearchObject,
        CompanyPriceInsertRequest, CompanyPriceUpdateRequest>{
    CompanyPriceDTO getCurrentPrice();
}
