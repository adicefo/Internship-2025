package com.example.internship_api.service.implementations;

import com.example.internship_api.PagedResult;
import com.example.internship_api.data.model.User;
import com.example.internship_api.data.model.request.UserInsertRequest;
import com.example.internship_api.data.model.request.UserUpdateRequest;
import com.example.internship_api.data.model.search_object.UserSearchObject;
import com.example.internship_api.repository.UserRepository;
import com.example.internship_api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User save(UserInsertRequest request) {
        return null;
    }

    @Override
    public User updateById(Long id, UserUpdateRequest request) {
        return null;
    }

    @Override
    public User deleteById(Long id) {
        return null;
    }

    @Override
    public PagedResult<User> getAll(UserSearchObject search) {

        return null;
    }

    @Override
    public User getById(int id) {
        return null;
    }


}
