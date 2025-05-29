package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.ClientDTO;
import com.example.internship_api.dto.DriverDTO;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.dto.UserUpdateRequest;
import com.example.internship_api.data.search_object.ClientSearchObject;
import com.example.internship_api.dto.DriverSearchObject;
import com.example.internship_api.entity.Client;
import com.example.internship_api.entity.Driver;
import com.example.internship_api.entity.User;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.exception.PasswordNotMatchException;
import com.example.internship_api.repository.ClientRepository;
import com.example.internship_api.repository.DriverRepository;
import com.example.internship_api.repository.UserRepository;
import com.example.internship_api.service.ClientService;
import com.example.internship_api.service.DriverService;
import com.example.internship_api.utils.PasswordUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl extends BaseCRUDServiceImpl<DriverDTO, DriverSearchObject, Driver, UserInsertRequest, UserUpdateRequest> implements DriverService {

    @Autowired
    private UserRepository userRepository;

    public DriverServiceImpl(DriverRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, DriverDTO.class, Driver.class);
    }
    @Override
    public DriverDTO saveBasedOnUser(Long userId) {
        var user=userRepository.findById(userId);
        if(!user.isPresent()){
            throw new EntityNotFoundException(userId, User.class);
        }
        var unwrappUser=user.get();
        var entity=new Driver();
        entity.setUser(unwrappUser);
        repository.save(entity);
        return modelMapper.map(entity, DriverDTO.class);
    }
    @Override
    protected void beforeInsert(UserInsertRequest request, Driver entity) {
        if(!request.getPassword().equals(request.getPasswordConfirm())){
            throw new PasswordNotMatchException();
        }
        User user=modelMapper.map(request,User.class);
        user.setPasswordSalt(PasswordUtils.generateSalt());
        user.setPasswordHash(PasswordUtils.generateHash(user.getPasswordSalt(),request.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());
        user.setActive(true);
        userRepository.save(user);
        entity.setUser(user);
    }

    @Override
    protected void beforeUpdate(UserUpdateRequest request, Driver entity) {

    }

    @Override
    protected void addFilter(DriverSearchObject search, List<Driver> query) {
        if (search == null) {
            return;
        }
        List<Driver> filteredQuery = query.stream()
                .filter(item-> search.getName() == null || item.getUser().getName().startsWith(search.getName()))
                .filter(item -> search.getSurname() == null || item.getUser().getSurname().startsWith(search.getSurname()))
                .collect(Collectors.toList());
if(search.getPageNumber()!=null&&search.getPageSize()!=null)
        {
        Pageable pageable=PageRequest.of(search.getPageNumber(),search.getPageSize());
        filteredQuery=repository.findAll(pageable).toList();
        }
        query.clear();
        query.addAll(filteredQuery);

    }


}
