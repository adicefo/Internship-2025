package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.model.search_object.BaseSearchObject;
import com.example.internship_api.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class BaseCRUDServiceImpl<TModel,TSearch extends BaseSearchObject,TDbEntity,TInsert,TUpdate>
extends BaseServiceImpl<TModel,TSearch,TDbEntity> {


    public BaseCRUDServiceImpl(JpaRepository<TDbEntity, Long> repository, ModelMapper modelMapper,
                               Class<TModel> modelClass, Class<TDbEntity> dbEntityClass) {
        super(repository, modelMapper, modelClass, dbEntityClass);

    }

    public TModel save(TInsert request) {
        TDbEntity entity = modelMapper.map(request, dbEntityClass);

        beforeInsert(request, entity);

        repository.save(entity);

        return modelMapper.map(entity, modelClass);
    }




    public TModel updateById(Long id, TUpdate request) {
        Optional<TDbEntity> entity=repository.findById(id);
        TDbEntity unwrapEntity=unwrapEntity(entity,id);

        modelMapper.map(request,unwrapEntity);


        beforeUpdate(request,unwrapEntity);

        repository.save(unwrapEntity);

        var result=modelMapper.map(unwrapEntity,modelClass);
        return result;
    }

    public TModel deleteById(Long id) {
        Optional<TDbEntity> entity=repository.findById(id);
        TDbEntity unwrapEntity=unwrapEntity(entity,id);


        repository.delete(unwrapEntity);


        var result=modelMapper.map(unwrapEntity,modelClass);
        return result;
    }


    protected abstract void beforeInsert(TInsert request, TDbEntity entity);
    protected abstract void beforeUpdate(TUpdate request, TDbEntity entity);
}
