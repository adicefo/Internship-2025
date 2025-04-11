package com.example.internship_api.delegate;

import com.example.internship_api.api.ClientApiDelegate;
import com.example.internship_api.dto.ClientDTO;
import com.example.internship_api.dto.ClientSearchObject;
import com.example.internship_api.dto.GetClient200Response;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientDelegateImpl implements ClientApiDelegate {

    @Autowired
    private ClientService service;

    @Override
    public ResponseEntity<GetClient200Response> getClient(ClientSearchObject search) {
        GetClient200Response response=new GetClient200Response();
        response.setItems(service.getAll(search).getResult());
        response.setCount(service.getAll(search).getCount());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ClientDTO> createClient(UserInsertRequest userInsertRequest) {
        return new ResponseEntity<>(service.save(userInsertRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ClientDTO> createClientBasedOnUser(Integer userId) {
        return new ResponseEntity<>(service.saveBasedOnUser(userId.longValue()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ClientDTO> getClientById(Integer id) {
        return new ResponseEntity<>(service.getById(id.longValue()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ClientDTO> deleteClient(Integer id) {
        return new ResponseEntity<>(service.deleteById(id.longValue()), HttpStatus.OK);
    }
}
