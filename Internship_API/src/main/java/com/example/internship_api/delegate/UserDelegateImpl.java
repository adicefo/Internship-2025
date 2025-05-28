package com.example.internship_api.delegate;



import com.example.internship_api.api.UsersApiDelegate;
import com.example.internship_api.dto.*;
import com.example.internship_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserDelegateImpl implements UsersApiDelegate {
    @Autowired
    private  UserService service;

    @Override
    public ResponseEntity<GetUsers200Response> getUsers(UserSearchObject search) {
        GetUsers200Response response = new GetUsers200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserDTO> createUser(UserInsertRequest userInsertRequest) {
        return new ResponseEntity<>(service.save(userInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(Integer id, UserUpdateRequest userUpdateRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), userUpdateRequest), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<UserDTO> updatePassword(Integer id,UserUpdatePasswordRequest request){
        return new  ResponseEntity<>(service.updatePassword(id.longValue(), request),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> deleteUser(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
