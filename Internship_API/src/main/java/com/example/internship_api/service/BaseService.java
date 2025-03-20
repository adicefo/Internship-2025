package com.example.internship_api.service;

import com.example.internship_api.PagedResult;
import com.example.internship_api.data.model.search_object.BaseSearchObject;

public interface BaseService<TModel, TSearch extends BaseSearchObject> {
    PagedResult<TModel> getAll(TSearch search);
    TModel getById(int id);
}
