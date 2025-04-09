package com.example.internship_api.service.implementations;

import com.example.internship_api.data.search_object.BaseSearchObject;
import com.example.internship_api.entity.Rent;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public abstract class BaseCRUDServiceImpl<TModel,TSearch,TDbEntity,TInsert,TUpdate>
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

        //added this because of modelMapper bug
        LocalDateTime originalRentDate=LocalDateTime.now();
        if(dbEntityClass== Rent.class)
            originalRentDate=((Rent)unwrapEntity).getRentDate();

        modelMapper.map(request,unwrapEntity);


        //added this because of modelMapper bug
        if(dbEntityClass== Rent.class)
            ((Rent)unwrapEntity).setRentDate(originalRentDate);

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
