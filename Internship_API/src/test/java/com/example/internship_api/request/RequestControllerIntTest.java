package com.example.internship_api.request;

import com.example.internship_api.dto.GetRequests200Response;
import com.example.internship_api.dto.RequestDTO;
import com.example.internship_api.dto.RequestInsertRequest;
import com.example.internship_api.dto.RequestUpdateRequest;
import com.example.internship_api.dto.RequestSearchObject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
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
public class RequestControllerIntTest {
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
    void shouldReturnAllRequests() {
        ResponseEntity<GetRequests200Response> response = restTemplate.exchange(
                "/v2/requests/get",
                HttpMethod.GET,
                null,
                GetRequests200Response.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCount()).isEqualTo(1);
    }

    @Test
    @Rollback
    void shouldCreateAndReturnRequest() {
        RequestInsertRequest request = new RequestInsertRequest();
        request.setDriverId(1);
        request.setRouteId(1);

        ResponseEntity<RequestDTO> requestItem = restTemplate.postForEntity(
                "/v2/requests/save",
                new HttpEntity<>(request),
                RequestDTO.class
        );

        assertThat(requestItem.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(requestItem.getBody()).isNotNull();
        assertThat(requestItem.getBody().getAccepted()).isEqualTo(null);

        ResponseEntity<RequestDTO> response = restTemplate.exchange(
                "/v2/requests/getById/1",
                HttpMethod.GET,
                null,
                RequestDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1);
    }



    @Test
    @Rollback
    void shouldUpdateRequest() {

        RequestUpdateRequest request = new RequestUpdateRequest();
        request.setAccepted(false);

        ResponseEntity<RequestDTO> response = restTemplate.exchange(
                "/v2/requests/update/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                RequestDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAccepted()).isEqualTo(false);
    }

    @Test
    @Rollback
    void shouldDeleteRequest() {
        ResponseEntity<RequestDTO> response = restTemplate.exchange(
                "/v2/requests/delete/1",
                HttpMethod.DELETE,
                null,
                RequestDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
