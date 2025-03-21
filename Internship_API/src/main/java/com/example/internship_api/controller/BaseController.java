package com.example.internship_api.controller;

import com.example.internship_api.PagedResult;
import com.example.internship_api.data.model.search_object.BaseSearchObject;
import com.example.internship_api.service.BaseService;
import com.example.internship_api.service.UserService;
import com.example.internship_api.service.implementations.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class BaseController<TModel,TSearch extends BaseSearchObject> {
    protected UserService service;



    public PagedResult<TModel> getAll(TSearch search) {
        return service.getAll(search);
    }

    public TModel getById(int id) {
        return service.getById(id);
    }
}
