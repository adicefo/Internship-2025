package com.example.internship_api.delegate;

import com.example.internship_api.api.AdminApiDelegate;
import com.example.internship_api.dto.*;
import com.example.internship_api.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminDelegateImpl implements AdminApiDelegate {
    @Autowired
    private AdminService service;

    @Override
    public ResponseEntity<GetAdmin200Response> getAdmin(AdminSearchObject search) {
        GetAdmin200Response response = new GetAdmin200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AdminDTO> createAdmin(UserInsertRequest userInsertRequest) {
        return new ResponseEntity<>(service.save(userInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AdminDTO> getAdminById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AdminDTO> updateAdmin(Integer id, UserUpdateRequest userUpdateRequest) {
        return new ResponseEntity<>(service.updateById(id.longValue(), userUpdateRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AdminDTO> deleteAdmin(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AdminDTO> createAdminBasedOnUser(Integer userId) {
        return new ResponseEntity<>(service.saveBasedOnUser(userId.longValue()), HttpStatus.CREATED);
    }
}
