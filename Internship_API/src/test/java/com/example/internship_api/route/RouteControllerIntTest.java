package com.example.internship_api.route;

import com.example.internship_api.dto.*;
import com.example.internship_api.entity.Route;
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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RouteControllerIntTest {
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
    void shouldReturnRoutes() {


        ResponseEntity<GetRoutes200Response> response = restTemplate.getForEntity("/v2/route/get", GetRoutes200Response.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetRouteById() {
        ResponseEntity<RouteDTO> response = restTemplate.exchange("/v2/route/getById/1", HttpMethod.GET, null, RouteDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCreateRouteWhenRouteIsValid() {
        RouteInsertRequest newRequest = new RouteInsertRequest();
        newRequest.setSourcePointLat(44.12313);
        newRequest.setSourcePointLon(17.85678);
        newRequest.setDestinationPointLat(44.3211);
        newRequest.setDestinationPointLon(18.12831);
        newRequest.setClientId(1);
        newRequest.setDriverId(1);


        ResponseEntity<RouteDTO> response = restTemplate.postForEntity("/v2/route/save", newRequest, RouteDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEndDate()).isNull();
    }

    @Test
    void shouldNotCreateRouteWhenRouteIsInvalid()
    {
        RouteInsertRequest newRequest = new RouteInsertRequest();
        newRequest.setSourcePointLat(44.12313);
        newRequest.setSourcePointLon(190.0);
        newRequest.setDestinationPointLat(-91.3211);
        newRequest.setDestinationPointLon(18.12831);
        newRequest.setClientId(1);
        newRequest.setDriverId(1);

        ResponseEntity<Object> response = restTemplate.postForEntity("/v2/route/save", newRequest, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @Rollback
    void shouldUpdateRouteWhenRouteIsValid() {
        RouteUpdateRequest newRequest = new RouteUpdateRequest();
       newRequest.setClientId(1);
       newRequest.setDriverId(1);

        ResponseEntity<RouteDTO> response = restTemplate.exchange(
                "/v2/route/update/1",
                HttpMethod.PUT,
                new HttpEntity<>(newRequest),
                RouteDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDriver().getId()).isEqualTo(1);
        assertThat(response.getBody().getStatus()).isEqualTo("active");
    }

    @Test
    @Rollback
    void shouldDeleteRoute() {
        ResponseEntity<RouteDTO> response = restTemplate.exchange("/v2/route/delete/1", HttpMethod.DELETE, null, RouteDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Rollback
    void shouldMarkRouteAsFinishedAfterActive() {
        // First, we need to update the route to set its status to "active"
        RouteUpdateRequest newRequest = new RouteUpdateRequest();
        newRequest.setClientId(1);
        newRequest.setDriverId(1);

       ResponseEntity<RouteDTO> route=restTemplate.exchange(
               "/v2/route/update/1",
               HttpMethod.PUT,
                new HttpEntity<>(newRequest),
               RouteDTO.class
       );

       assertThat(route.getBody()).isNotNull();
       assertThat(route.getBody().getStatus()).isEqualTo("active");


        ResponseEntity<RouteDTO> response = restTemplate.exchange(
                "/v2/route/updateFinish/1",
                HttpMethod.PUT,
                null,
                RouteDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStatus()).isEqualTo("finished");
    }

    @Test
    @Rollback
    void shouldMarkRouteAsPaid() {
        ResponseEntity<RouteDTO> response = restTemplate.exchange(
                "/v2/route/updatePayment/1",
                HttpMethod.PUT,
                null,
                RouteDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPaid()).isEqualTo(true);
    }

    @Test
    void shouldReturnAmountForReportAfterFinishingRoute() {
        //Make route as active
        RouteUpdateRequest newRequest = new RouteUpdateRequest();
        newRequest.setClientId(1);
        newRequest.setDriverId(1);
        ResponseEntity<RouteDTO> routeResponse = restTemplate.exchange(
                "/v2/route/update/1",
                HttpMethod.PUT,
                new HttpEntity<>(newRequest),
                RouteDTO.class
        );

        assertThat(routeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(routeResponse.getBody().getStatus()).isEqualTo("active");

        //Make route as finished
        ResponseEntity<RouteDTO> finishResponse = restTemplate.exchange(
                "/v2/route/updateFinish/1",
                HttpMethod.PUT,
                null,
                RouteDTO.class
        );
        assertThat(finishResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(finishResponse.getBody().getStatus()).isEqualTo("finished");


        GeneralReportRequest reportRequest = new GeneralReportRequest();
        reportRequest.setMonth(4);
        reportRequest.setYear(2025);

        ResponseEntity<Map> response = restTemplate.exchange(
                "/v2/route/getAmountForReport",
                HttpMethod.POST,
                new HttpEntity<>(reportRequest),
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("fullAmount").toString()).isGreaterThan("0");
    }
}
