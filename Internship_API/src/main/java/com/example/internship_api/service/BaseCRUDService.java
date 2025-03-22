package com.example.internship_api.service;

import com.example.internship_api.data.search_object.BaseSearchObject;

public interface BaseCRUDService<TModel, TSearch extends BaseSearchObject,TInsert,TUpdate> extends BaseService<TModel,TSearch> {
    TModel save(TInsert request);
    TModel updateById(Long id, TUpdate request);
    TModel deleteById(Long id);
}