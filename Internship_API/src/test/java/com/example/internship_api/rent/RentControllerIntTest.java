package com.example.internship_api.rent;


import com.example.internship_api.dto.*;
import com.example.internship_api.entity.Rent;
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

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RentControllerIntTest {
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
    void shouldReturnRents() {
        ResponseEntity<GetRents200Response> response = restTemplate.getForEntity(
                "/v2/rent/get",  GetRents200Response.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCount()).isEqualTo(7);
    }

    @Test
    void shouldGetRentById() {
        ResponseEntity<RentDTO> response = restTemplate.getForEntity("/v2/rent/getById/1", RentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

    @Test
    @Rollback
    void shouldCreateRentWhenRentIsValid() {
        RentInsertRequest request = new RentInsertRequest();
        request.setClientId(1);
        request.setVehicleId(1);
        request.setRentDate(LocalDateTime.now().plusDays(5));
        request.setEndDate(LocalDateTime.now().plusDays(15));


        ResponseEntity<RentDTO> response = restTemplate.postForEntity("/v2/rent/save", request, RentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getClient().getId()).isEqualTo(1);
        assertThat(response.getBody().getNumberOfDays()).isEqualTo(10);
        assertThat(response.getBody().getFullPrice()).isEqualTo(response.getBody().getNumberOfDays()*response.getBody().getVehicle().getPrice());
    }
    @Test
    void shouldNotCreateRentWhenRentIsInvalid()
    {
        RentInsertRequest request = new RentInsertRequest();
        request.setClientId(1);
        request.setVehicleId(1);
        request.setRentDate(LocalDateTime.now().plusDays(5));
        request.setEndDate(LocalDateTime.now().plusDays(2));

        ResponseEntity<Object> response = restTemplate.postForEntity("/v2/rent/save", request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    @Rollback
    void shouldUpdateRentWhenValid() {
        RentUpdateRequest request = new RentUpdateRequest();
        request.setVehicleId(1);
        request.setEndDate(LocalDateTime.now().plusDays(15));

        ResponseEntity<RentDTO> response = restTemplate.exchange(
                "/v2/rent/update/1", HttpMethod.PUT, new HttpEntity<>(request), RentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNumberOfDays()).isEqualTo(5);
    }

    @Test
    @Rollback
    void shouldDeleteRent() {
        ResponseEntity<RentDTO> response = restTemplate.exchange(
                "/v2/rent/delete/1", HttpMethod.DELETE, null, RentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldCheckRentAvailability() {
        // First accepting request on particular date
        ResponseEntity<RentDTO> response = restTemplate.exchange(
                "/v2/rent/updateActive/4", HttpMethod.PUT, null, RentDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStatus()).isEqualTo("active");

        //Creating new rent request
        RentInsertRequest request = new RentInsertRequest();
        request.setVehicleId(4);
        request.setClientId(2);
        request.setRentDate(LocalDateTime.now().plusDays(5));
        request.setEndDate(LocalDateTime.now().plusDays(8));

        ResponseEntity<RentDTO> rentResponse = restTemplate.postForEntity(
                "/v2/rent/save", new HttpEntity<>(request), RentDTO.class);

        // Now checking availability for the same date
        RentAvailabilityRequest availabilityRequest = new RentAvailabilityRequest();
        availabilityRequest.setVehicleId(rentResponse.getBody().getVehicle().getId());
        availabilityRequest.setRentDate(rentResponse.getBody().getRentDate());
        availabilityRequest.setEndDate(rentResponse.getBody().getEndDate());

        ResponseEntity<Map> availabilityResponse = restTemplate.postForEntity(
                "/v2/rent/checkAvailability/"+rentResponse.getBody().getId(), new HttpEntity<>(availabilityRequest), Map.class);

        assertThat(availabilityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(availabilityResponse.getBody().get("isAvailable")).isEqualTo(false);
    }

    @Test
    @Rollback
    void shouldUpdateRentToActive() {
        ResponseEntity<RentDTO> response = restTemplate.exchange(
                "/v2/rent/updateActive/1", HttpMethod.PUT, null, RentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStatus()).isEqualTo("active");
    }

    @Test
    @Rollback
    void shouldUpdateRentToFinished() {
        restTemplate.exchange("/v2/rent/updateActive/1", HttpMethod.PUT, null, RentDTO.class);

        ResponseEntity<RentDTO> response = restTemplate.exchange(
                "/v2/rent/updateFinish/1", HttpMethod.PUT, null, RentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStatus()).isEqualTo("finished");
    }

    @Test
    @Rollback
    void shouldUpdateRentToPaid() {
        ResponseEntity<RentDTO> response = restTemplate.exchange(
                "/v2/rent/updatePayment/1", HttpMethod.PUT, null, RentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPaid()).isTrue();
    }
}
