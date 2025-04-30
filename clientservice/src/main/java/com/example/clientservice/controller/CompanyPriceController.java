package com.example.clientservice.controller;

import com.example.clientservice.model.CompanyPriceDTO;
import com.example.clientservice.model.CompanyPriceInsertRequest;
import com.example.clientservice.model.CompanyPriceSearchObject;
import com.example.clientservice.model.GetCompanyPrices200Response;
import com.example.clientservice.service.CompanyPriceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companyPrice")
public class CompanyPriceController {
    @Autowired
    private CompanyPriceService service;

    @GetMapping("/get")
    public ResponseEntity<GetCompanyPrices200Response> getCompanyPrice(CompanyPriceSearchObject search) {
       return new ResponseEntity<>(service.getCompanyPrices(search), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CompanyPriceDTO> getCompanyPriceById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getCompanyPriceById(id), HttpStatus.OK);
    }

    @GetMapping("/getCurrent")
    public ResponseEntity<CompanyPriceDTO> getCurrentCompanyPrice() {
        return new ResponseEntity<>(service.getCurrentCompanyPrice(), HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<CompanyPriceDTO> createCompanyPrice(@RequestBody @Valid CompanyPriceInsertRequest request) {
        return new ResponseEntity<>(service.saveCompanyPrice(request), HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CompanyPriceDTO> deleteCompanyPrice(@PathVariable Integer id) {
        return new ResponseEntity<>(service.deleteCompanyPrice(id), HttpStatus.OK);
    }
}
