package com.example.internship_api.statistics;

import com.example.internship_api.dto.GetStatistics200Response;
import com.example.internship_api.dto.StatisticsDTO;
import com.example.internship_api.dto.StatisticsInsertRequest;
import com.example.internship_api.dto.StatisticsUpdateRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
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


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class StatisticsControllerIntTest {

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
    void shouldReturnStatistics() {

        ResponseEntity<GetStatistics200Response> response = restTemplate.exchange(
                "/v2/statistics/get",
                HttpMethod.GET,
                null,
                GetStatistics200Response.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getItems()).isNotNull();
        System.out.println(response.getBody().getItems());
    }
    @Test
    void shouldReturnStatisticsById() {
        ResponseEntity<StatisticsDTO> response = restTemplate.exchange(
                "/v2/statistics/getById/1",
                HttpMethod.GET,
                null,
                StatisticsDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

    @Test
    @Rollback
    void shouldCreateStatisticsWhenStatisticsIsValid() {
        StatisticsInsertRequest request = new StatisticsInsertRequest();
        request.setDriverId(1);


        ResponseEntity<StatisticsDTO> response = restTemplate.postForEntity(
                "/v2/statistics/save",
                new HttpEntity<>(request),
                StatisticsDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldUpdateStatistics() {
        StatisticsUpdateRequest request = new StatisticsUpdateRequest();
        request.setEndOfWork(LocalDateTime.now().plusDays(10));

        ResponseEntity<StatisticsDTO> response = restTemplate.exchange(
                "/v2/statistics/update/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                StatisticsDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldUpdateFinishField() {
        ResponseEntity<StatisticsDTO> response = restTemplate.exchange(
                "/v2/statistics/updateFinish/1",
                HttpMethod.PUT,
                null,
                StatisticsDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEndOfWork()).isNotNull();
    }

    @Test
    @Rollback
    void shouldDeleteStatistics() {
        ResponseEntity<StatisticsDTO> response = restTemplate.exchange(
                "/v2/statistics/delete/1",
                HttpMethod.DELETE,
                null,
                StatisticsDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}
