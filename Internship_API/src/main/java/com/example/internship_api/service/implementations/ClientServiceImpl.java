package com.example.internship_api.service.implementations;


import com.example.internship_api.data.model.AdminDTO;
import com.example.internship_api.dto.ClientDTO;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.dto.UserUpdateRequest;
import com.example.internship_api.data.search_object.AdminSearchObject;
import com.example.internship_api.dto.ClientSearchObject;
import com.example.internship_api.entity.Admin;
import com.example.internship_api.entity.Client;
import com.example.internship_api.entity.User;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.exception.PasswordNotMatchException;
import com.example.internship_api.repository.AdminRepository;
import com.example.internship_api.repository.ClientRepository;
import com.example.internship_api.repository.UserRepository;
import com.example.internship_api.service.AdminService;
import com.example.internship_api.service.ClientService;
import com.example.internship_api.utils.PasswordUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl extends BaseCRUDServiceImpl<ClientDTO, ClientSearchObject, Client, UserInsertRequest, UserUpdateRequest> implements ClientService {
    @Autowired
    private UserRepository userRepository;

    public ClientServiceImpl(ClientRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, ClientDTO.class, Client.class);
    }
    @Override
    public ClientDTO saveBasedOnUser(Long userId) {
        var user=userRepository.findById(userId);
        if(!user.isPresent()){
            throw new EntityNotFoundException(userId, User.class);
        }
        var unwrappUser=user.get();
        var entity=new Client();
        entity.setUser(unwrappUser);
        repository.save(entity);
        return modelMapper.map(entity, ClientDTO.class);
    }

    @Override
    protected void beforeInsert(UserInsertRequest request, Client entity) {
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
    protected void beforeUpdate(UserUpdateRequest request, Client entity) {

    }

    @Override
    protected void addFilter(ClientSearchObject search, List<Client> query) {
        if (search == null) {
            return;
        }

        List<Client> filteredQuery = query.stream()
                .filter(item-> search.getName() == null || item.getUser().getName().startsWith(search.getName()))
                .filter(item -> search.getSurname() == null || item.getUser().getSurname().startsWith(search.getSurname()))
                .collect(Collectors.toList());
        if (search.getPageNumber() != null && search.getPageSize() != null) {
        int start = search.getPageNumber() * search.getPageSize();
        int end = Math.min(start + search.getPageSize(), filteredQuery.size());

        if (start < end) {
            filteredQuery = filteredQuery.subList(start, end);
        } else {
            filteredQuery = new ArrayList<>();
        }
    }
        query.clear();
        query.addAll(filteredQuery);
    }


}
