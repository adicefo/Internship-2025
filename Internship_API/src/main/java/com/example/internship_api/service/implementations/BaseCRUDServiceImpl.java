package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.model.search_object.BaseSearchObject;
import com.example.internship_api.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseCRUDServiceImpl<TModel,TSearch extends BaseSearchObject,TDbEntity,TInsert,TUpdate>
extends BaseServiceImpl<TModel,TSearch,TDbEntity> {
    private final JpaRepository<TDbEntity, Long> repository;
    private final ModelMapper modelMapper;
    private final Class<TModel> modelClass;
    private final Class<TDbEntity> dbEntityClass;

    public BaseCRUDServiceImpl(JpaRepository<TDbEntity, Long> repository, ModelMapper modelMapper,
                               Class<TModel> modelClass, Class<TDbEntity> dbEntityClass) {
        super(repository, modelMapper, modelClass, dbEntityClass);
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.modelClass = modelClass;
        this.dbEntityClass = dbEntityClass;
    }

    public TModel save(TInsert request) {
        TDbEntity entity = modelMapper.map(request, dbEntityClass);

        beforeInsert(request, entity);

        repository.save(entity);

        return modelMapper.map(entity, modelClass);
    }




    public TModel updateById(Long id, TUpdate request) {
        return null;
    }

    public TModel deleteById(Long id) {
        return null;
    }
    protected abstract void beforeInsert(TInsert request, TDbEntity entity);

}
