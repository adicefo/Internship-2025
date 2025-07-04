package com.example.internship_api.service.implementations;

import com.example.internship_api.data.PagedResult;
import com.example.internship_api.data.search_object.BaseSearchObject;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.service.BaseService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public abstract class BaseServiceImpl<TModel,TSearch,TDbEntity> implements BaseService<TModel,TSearch> {
    protected final JpaRepository<TDbEntity,Long> repository;
    protected   final ModelMapper modelMapper;

    protected final Class<TModel> modelClass;  // Store class type
    protected final Class<TDbEntity> dbEntityClass;

    public BaseServiceImpl(JpaRepository<TDbEntity, Long> repository, ModelMapper modelMapper,
                           Class<TModel> modelClass, Class<TDbEntity> dbEntityClass) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.modelClass = modelClass;
        this.dbEntityClass = dbEntityClass;
    }


    public PagedResult<TModel> getAll(TSearch search) {
        var query=repository.findAll();
        addFilter(search,query);
        int count=repository.findAll().size();
        List<TModel> result = query.stream()
                .map(item -> modelMapper.map(item, modelClass))
                .collect(Collectors.toList());
        return new PagedResult<>(result,count);

    }



    @Override
    public TModel getById(Long id) {
        Optional<TDbEntity> entity=repository.findById(id);
        TDbEntity unwrapEntity=unwrapEntity(entity,id);

        var result=modelMapper.map(unwrapEntity,modelClass);
        return result;

    }
   protected TDbEntity unwrapEntity(Optional<TDbEntity> entity, Long entityId) {
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(entityId, dbEntityClass);
    }
    protected abstract void addFilter(TSearch search, List<TDbEntity> query);
}
