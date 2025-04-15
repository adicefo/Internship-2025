package com.example.internship_api.review;

import com.example.internship_api.dto.*;
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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ReviewControllerIntTest {

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
    void shouldReturnReviews() {
        ResponseEntity<GetReviews200Response> response = restTemplate.exchange(
                "/v2/review/get",
                HttpMethod.GET,
                null,
                GetReviews200Response.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getItems()).isNotEmpty();
        assertThat(response.getBody().getCount()).isGreaterThan(0);
    }

    @Test
    void shouldGetReviewById() {
        ResponseEntity<ReviewDTO> response = restTemplate.exchange(
                "/v2/review/getById/1",
                HttpMethod.GET,
                null,
                ReviewDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

    @Test
    @Rollback
    void shouldCreateReviewWhenReviewIsValid() {
        ReviewInsertRequest request = new ReviewInsertRequest();
        request.setValue(5);
        request.setDescription("Excellent service");
        request.setReviewsId(1);
        request.setReviewedId(1);

        ResponseEntity<ReviewDTO> response = restTemplate.postForEntity(
                "/v2/review/save",
                request,
                ReviewDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getValue()).isEqualTo(5);
    }
    @Test
    void shouldNotCreateReviewWhenReviewIsNotValid()
    {
        ReviewInsertRequest request = new ReviewInsertRequest();
        request.setValue(10);
        request.setDescription("Invalid review");
        request.setReviewsId(1);
        request.setReviewedId(1);

        ResponseEntity<Object> response = restTemplate.postForEntity(
                "/v2/review/save",
                request,
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnNullDriversForReportWhenThereIsNoReviewInThatPeriod() {
        GeneralReportRequest reportRequest = new GeneralReportRequest();
        reportRequest.setMonth(1);
        reportRequest.setYear(2025);

        ResponseEntity<Map> response = restTemplate.exchange(
                "/v2/review/getDriversForReport",
                HttpMethod.POST,
                new HttpEntity<>(reportRequest),
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("maxDriver")).isEqualTo("null");
        assertThat(response.getBody().get("minDriver")).isEqualTo("null");


    }

    @Test
    @Rollback
    void shouldUpdateReview() {
        ReviewUpdateRequest request = new ReviewUpdateRequest();
        request.setValue(3);
        request.setDescription("Updated feedback");

        ResponseEntity<ReviewDTO> response = restTemplate.exchange(
                "/v2/review/update/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ReviewDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getValue()).isEqualTo(3);
    }

    @Test
    @Rollback
    void shouldDeleteReview() {
        ResponseEntity<ReviewDTO> response = restTemplate.exchange(
                "/v2/review/delete/1",
                HttpMethod.DELETE,
                null,
                ReviewDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
