package com.example.internship_api.service.implementations;

import com.example.internship_api.PagedResult;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.model.search_object.BaseSearchObject;
import com.example.internship_api.data.model.search_object.UserSearchObject;
import com.example.internship_api.entity.User;
import com.example.internship_api.service.BaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class BaseServiceImpl<TModel,TSearch extends BaseSearchObject,TDbEntity> implements BaseService<TModel,TSearch> {
    private final JpaRepository<TDbEntity,Long> repository;
    private  final ModelMapper modelMapper;

    private final Class<TModel> modelClass;  // Store class type
    private final Class<TDbEntity> dbEntityClass;

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
        //adding pagination
        if (search!=null&&search.getPageNumber() != null && search.getPageSize() != null) {
            Pageable pageable = PageRequest.of(search.getPageNumber(), search.getPageSize());
            Page<TDbEntity> pageResult = repository.findAll(pageable);
            List<TModel> result=pageResult.getContent().stream().map(item->modelMapper.map(item,modelClass)).collect(Collectors.toList());
            return new PagedResult<>(result,count);
        }
        List<TModel> result = query.stream()
                .map(item -> modelMapper.map(item, modelClass))
                .collect(Collectors.toList());
        return new PagedResult<>(result,count);

    }

    protected abstract void addFilter(TSearch search, List<TDbEntity> query);


    @Override
    public TModel getById(int id) {
        return null;
    }
}
