package com.example.internship_api.service.implementations;

import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.model.request.UserInsertRequest;
import com.example.internship_api.data.model.request.UserUpdateRequest;
import com.example.internship_api.data.model.search_object.UserSearchObject;
import com.example.internship_api.entity.User;
import com.example.internship_api.repository.UserRepository;
import com.example.internship_api.service.UserService;
import com.example.internship_api.utils.PasswordUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends  BaseCRUDServiceImpl<UserDTO, UserSearchObject, User, UserInsertRequest, UserUpdateRequest> implements UserService {

    public UserServiceImpl(UserRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, UserDTO.class, User.class);
    }

    @Override
    protected void beforeInsert(UserInsertRequest request, User entity) {
        if(!request.password().equals(request.passwordConfirm())){
            throw new RuntimeException("Passwords do not match");
        }
        entity.setPasswordSalt(PasswordUtils.generateSalt());
        var passwordHash=PasswordUtils.generateHash(entity.getPasswordSalt(),request.password());
        entity.setPasswordHash(passwordHash);
        entity.setRegistrationDate(LocalDateTime.now());

    }
    @Override
    protected void beforeUpdate(UserUpdateRequest request, User entity) {
        if(request.password()!=null){
            if(!request.password().equals(request.passwordConfirm())){
                throw new RuntimeException("Passwords do not match");
            }
            entity.setPasswordSalt(PasswordUtils.generateSalt());
            var passwordHash=PasswordUtils.generateHash(entity.getPasswordSalt(),request.password());
            entity.setPasswordHash(passwordHash);
        }
    }
    @Override
    protected void addFilter(UserSearchObject search, List<User> query) {
        if (search == null) {
            return;
        }

        List<User> filteredQuery = query.stream()
                .filter(user -> search.getUsername() == null || user.getUsername().startsWith(search.getUsername()))
                .filter(user -> search.getEmail() == null || user.getEmail().startsWith(search.getEmail()))
                .filter(user -> search.getName() == null || user.getName().startsWith(search.getName()))
                .filter(user -> search.getSurname() == null || user.getSurname().startsWith(search.getSurname()))
                .collect(Collectors.toList());

        query.clear();
        query.addAll(filteredQuery);
    }

    /*@Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO save(UserInsertRequest request) {
        User entity = modelMapper.map(request, User.class);

        beforeInsert(request, entity);

        repository.save(entity);

        return modelMapper.map(entity, UserDTO.class);
    }

    @Override
    public UserDTO updateById(Long id, UserUpdateRequest request) {
        return null;
    }

    @Override
    public UserDTO deleteById(Long id) {
        return null;
    }

    @Override
    public PagedResult<UserDTO> getAll(UserSearchObject search) {
        var query=repository.findAll();
        addFilter(search,query);
        int count=repository.findAll().size();
        //adding pagination
        if (search!=null&&search.getPageNumber() != null && search.getPageSize() != null) {
            Pageable pageable = PageRequest.of(search.getPageNumber(), search.getPageSize());
            Page<User> pageResult = repository.findAll(pageable);
        List<UserDTO> result=pageResult.getContent().stream().map(item->modelMapper.map(item,UserDTO.class)).collect(Collectors.toList());
        return new PagedResult<>(result,count);
        }
        List<UserDTO> result=query.stream().map(item->modelMapper.map(item,UserDTO.class)).collect(Collectors.toList());
        return new PagedResult<>(result,count);

    }

    @Override
    public UserDTO getById(int id) {
        return null;
    }

    private void addFilter(UserSearchObject search, List<User> list) {

    }
    private void beforeInsert(UserInsertRequest request, User entity) {

    }
    private void beforeUpdate(UserUpdateRequest request, User entity) {

    }*/
}
