package com.example.internship_api.controller;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.model.CompanyPriceDTO;
import com.example.internship_api.data.model.RentDTO;
import com.example.internship_api.data.request.CompanyPriceInsertRequest;
import com.example.internship_api.data.request.RentInsertRequest;
import com.example.internship_api.data.search_object.CompanyPriceSearchObject;
import com.example.internship_api.data.search_object.RentSearchObject;
import com.example.internship_api.service.CompanyPriceService;
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
    public ResponseEntity<PagedResult<CompanyPriceDTO>> getAll(@ModelAttribute CompanyPriceSearchObject searchObject) {

        return new ResponseEntity<>(service.getAll(searchObject), HttpStatus.OK);
    }
    @GetMapping("/getCurrent")
    public ResponseEntity<CompanyPriceDTO> getCurrentPrice() {

        return new ResponseEntity<>(service.getCurrentPrice(), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CompanyPriceDTO> getById(@PathVariable Long id) {

        return new ResponseEntity<>(service.getById(id),HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<CompanyPriceDTO> save(@Valid @RequestBody CompanyPriceInsertRequest request) {

        return new ResponseEntity<>(service.save(request),HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CompanyPriceDTO> delete(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteById(id),HttpStatus.OK);
    }
}
