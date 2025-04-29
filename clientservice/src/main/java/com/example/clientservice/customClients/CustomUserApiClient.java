package com.example.clientservice.customClients;

import org.springframework.cloud.openfeign.FeignClient;
import com.example.clientservice.api.UsersApi;
import com.example.clientservice.config.FeignClientConfig;
@FeignClient(name="${users.name:users}", url="${users.url:http://localhost:8081/v2}")
public interface CustomUserApiClient extends UsersApi {
}