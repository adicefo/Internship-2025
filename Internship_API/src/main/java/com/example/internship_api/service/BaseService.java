package com.example.internship_api.service;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.search_object.BaseSearchObject;

public interface BaseService<TModel, TSearch> {
    PagedResult<TModel> getAll(TSearch search);
    TModel getById(Long id);
}
