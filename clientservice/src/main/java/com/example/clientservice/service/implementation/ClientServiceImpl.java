package com.example.clientservice.service.implementation;

import com.example.clientservice.api.ClientApiClient;
import com.example.clientservice.model.ClientDTO;
import com.example.clientservice.model.ClientSearchObject;
import com.example.clientservice.model.GetClient200Response;
import com.example.clientservice.model.UserInsertRequest;
import com.example.clientservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientApiClient clientApiClient;

    @Override
    public ClientDTO createClient(UserInsertRequest userInsertRequest) {
        ResponseEntity<ClientDTO> responseEntity = clientApiClient.createClient(userInsertRequest);
        return responseEntity.getBody();
    }

    @Override
    public ClientDTO createClientBasedOnUser(Integer userId) {
        ResponseEntity<ClientDTO> responseEntity = clientApiClient.createClientBasedOnUser(userId);
        return responseEntity.getBody();
    }

    @Override
    public ClientDTO deleteClient(Integer id) {
        ResponseEntity<ClientDTO> responseEntity = clientApiClient.deleteClient(id);
        return responseEntity.getBody();
    }

    @Override
    public ClientDTO getClientById(Integer id) {
        ResponseEntity<ClientDTO> responseEntity = clientApiClient.getClientById(id);
        return responseEntity.getBody();
    }

    @Override
    public GetClient200Response getClient(ClientSearchObject searchObject) {
        ResponseEntity<GetClient200Response>responseEntity = clientApiClient.getClient(searchObject);
        return responseEntity.getBody();
    }
}
