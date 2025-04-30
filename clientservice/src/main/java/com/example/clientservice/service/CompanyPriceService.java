package com.example.clientservice.service;

import com.example.clientservice.model.*;

public interface CompanyPriceService {
    CompanyPriceDTO saveCompanyPrice(CompanyPriceInsertRequest request);
    CompanyPriceDTO deleteCompanyPrice(Integer id);
    CompanyPriceDTO getCurrentCompanyPrice();
    GetCompanyPrices200Response getCompanyPrices(CompanyPriceSearchObject search);
    CompanyPriceDTO getCompanyPriceById(Integer id);
}
