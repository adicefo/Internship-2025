package com.example.internship_api.companyPrice;

import com.example.internship_api.dto.CompanyPriceDTO;
import com.example.internship_api.dto.CompanyPriceInsertRequest;
import com.example.internship_api.dto.GetCompanyPrices200Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CompanyPriceControllerIntTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void connectionEstablished(){
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldReturnAllCompanyPrices() {
        ResponseEntity<GetCompanyPrices200Response> response = restTemplate.getForEntity("/v2/companyPrice/get", GetCompanyPrices200Response.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetCurrentCompanyPrice() {
        ResponseEntity<CompanyPriceDTO> response = restTemplate.getForEntity("/v2/companyPrice/getCurrent", CompanyPriceDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isGreaterThan(1);
    }

    @Test
    @Rollback
    void shouldCreateCompanyPriceWhenPriceIsValid() {
        CompanyPriceInsertRequest newRequest = new CompanyPriceInsertRequest();
        newRequest.setPricePerKilometer(3.09);

        ResponseEntity<CompanyPriceDTO> response = restTemplate.postForEntity("/v2/companyPrice/save", new HttpEntity<>(newRequest), CompanyPriceDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPricePerKilometer()).isEqualTo(3.09);
    }

    @Test

    void shouldNotCreateCompanyPriceWhenPriceIsInvalid()
    {
        CompanyPriceInsertRequest newRequest = new CompanyPriceInsertRequest();
        newRequest.setPricePerKilometer(16.0);

        ResponseEntity<Object> response = restTemplate.postForEntity("/v2/companyPrice/save", new HttpEntity<>(newRequest), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldGetCompanyPriceById() {
        ResponseEntity<CompanyPriceDTO> response = restTemplate.exchange("/v2/companyPrice/getById/1", HttpMethod.GET, null, CompanyPriceDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldDeleteCompanyPriceById() {
        ResponseEntity<Object> response = restTemplate.exchange("/v2/companyPrice/delete/2", HttpMethod.DELETE, null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response.getBody());
    }
}

