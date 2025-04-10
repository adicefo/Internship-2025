package com.example.internship_api.service.implementations;


import com.example.internship_api.dto.AdminDTO;
import com.example.internship_api.data.model.ClientDTO;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.dto.UserUpdateRequest;
import com.example.internship_api.dto.AdminSearchObject;
import com.example.internship_api.data.search_object.UserSearchObject;
import com.example.internship_api.entity.Admin;
import com.example.internship_api.entity.Client;
import com.example.internship_api.entity.User;
import com.example.internship_api.exception.EntityNotFoundException;
import com.example.internship_api.exception.PasswordNotMatchException;
import com.example.internship_api.repository.AdminRepository;
import com.example.internship_api.repository.UserRepository;
import com.example.internship_api.service.AdminService;
import com.example.internship_api.service.UserService;
import com.example.internship_api.utils.PasswordUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl extends BaseCRUDServiceImpl<AdminDTO, AdminSearchObject, Admin, UserInsertRequest, UserUpdateRequest> implements AdminService {

    @Autowired
    private UserRepository userRepository;

    public AdminServiceImpl(AdminRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, AdminDTO.class, Admin.class);
    }

    @Override
    public AdminDTO saveBasedOnUser(Long userId) {
        var user=userRepository.findById(userId);
        if(!user.isPresent()){
            throw new EntityNotFoundException(userId, User.class);
        }
        var unwrappUser=user.get();
        var entity=new Admin();
        entity.setUser(unwrappUser);
        repository.save(entity);
        return modelMapper.map(entity, AdminDTO.class);
    }
    @Override
    protected void beforeInsert(UserInsertRequest request, Admin entity) {
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
    protected void beforeUpdate(UserUpdateRequest request, Admin entity) {
        if(request.getPassword()!=null){
            if(!request.getPassword().equals(request.getPasswordConfirm())){
                throw new PasswordNotMatchException();
            }
            entity.getUser().setPasswordSalt(PasswordUtils.generateSalt());
            var passwordHash=PasswordUtils.generateHash(entity.getUser().getPasswordSalt(),request.getPassword());
            entity.getUser().setPasswordHash(passwordHash);

        }
    }

    @Override
    protected void addFilter(AdminSearchObject search, List<Admin> query) {
        if (search == null) {
            return;
        }

        List<Admin> filteredQuery = query.stream()
                .filter(item-> search.getName() == null || item.getUser().getName().startsWith(search.getName()))
                .filter(item -> search.getSurname() == null || item.getUser().getSurname().startsWith(search.getSurname()))
                .collect(Collectors.toList());

        query.clear();
        query.addAll(filteredQuery);

    }


}
