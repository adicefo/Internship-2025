package com.example.internship_api.delegate;

import com.example.internship_api.api.ClientApiDelegate;
import com.example.internship_api.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientDelegateImpl implements ClientApiDelegate {

    @Autowired
    private ClientService service;
}
