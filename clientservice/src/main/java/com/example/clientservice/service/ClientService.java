package com.example.clientservice.service;

import com.example.clientservice.model.ClientDTO;
import com.example.clientservice.model.ClientSearchObject;
import com.example.clientservice.model.GetClient200Response;
import com.example.clientservice.model.UserInsertRequest;

public interface ClientService {
    ClientDTO createClient(UserInsertRequest userInsertRequest);
    ClientDTO createClientBasedOnUser(Integer userId);
    ClientDTO deleteClient(Integer id);
    ClientDTO getClientById(Integer id);
    GetClient200Response getClient(ClientSearchObject searchObject);

}
