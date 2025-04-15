package com.example.internship_api.driverVehicle;


import com.example.internship_api.dto.*;
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

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class DriverVehicleControllerIntTest {
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
    void shouldReturnAllDriverVehicles() {
        ResponseEntity<GetDriverVehicles200Response> response = restTemplate.exchange(
                "/v2/driverVehicle/get",
                HttpMethod.GET,
                null,
                GetDriverVehicles200Response.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getItems()).isNotNull();
        assertThat(response.getBody().getCount()).isEqualTo(1);
    }
    @Test
    void shouldReturnDriverVehicle() {
        ResponseEntity<DriverVehicleDTO> getResponse = restTemplate.exchange(
                "/v2/driverVehicle/getById/1",
                HttpMethod.GET,
                null,
                DriverVehicleDTO.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getId()).isEqualTo(1);
    }

    @Test
    @Rollback
    void shouldCreateEntityIfEntityIsValid() {
        DriverVehicleInsertRequest request = new DriverVehicleInsertRequest();
        request.setDriverId(1);
        request.setVehicleId(1);

        ResponseEntity<DriverVehicleDTO> response = restTemplate.postForEntity(
                "/v2/driverVehicle/save",
                new HttpEntity<>(request),
                DriverVehicleDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

    }

    @Test
    @Rollback
    void shouldUpdateFinishDriverVehicle() {
        // First, insert some data
        DriverVehicleInsertRequest insertRequest = new DriverVehicleInsertRequest();
        insertRequest.setDriverId(1);
        insertRequest.setVehicleId(4);

        ResponseEntity<DriverVehicleDTO> created = restTemplate.postForEntity(
                "/v2/driverVehicle/save",
                new HttpEntity<>(insertRequest),
                DriverVehicleDTO.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody()).isNotNull();

        //Finish the driver vehicle
        DriverVehicleFinishRequest finishRequest = new DriverVehicleFinishRequest();
        finishRequest.setDriverId(1);
        finishRequest.setDatePick(created.getBody().getDatePick());

        ResponseEntity<DriverVehicleDTO> response = restTemplate.exchange(
                "/v2/driverVehicle/updateFinish",
                HttpMethod.PUT,
                new HttpEntity<>(finishRequest),
                DriverVehicleDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDateDrop()).isNotNull();
    }

    @Test
    @Rollback
    void shouldDeleteDriverVehicle() {
        ResponseEntity<DriverVehicleDTO> response = restTemplate.exchange(
                "/v2/driverVehicle/delete/1",
                HttpMethod.DELETE,
                null,
                DriverVehicleDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldCheckIfDriverAssignedWhenHeIsAssigned() {
        DriverVehicleInsertRequest request = new DriverVehicleInsertRequest();
        request.setDriverId(1);
        request.setVehicleId(4);
        ResponseEntity<DriverVehicleDTO> newInstance= restTemplate.postForEntity(
                "/v2/driverVehicle/save",
                new HttpEntity<>(request),
                DriverVehicleDTO.class
        );
        assertThat(newInstance.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newInstance.getBody()).isNotNull();

        ResponseEntity<Map> response = restTemplate.exchange(
                "/v2/driverVehicle/checkIfAssigned/"+newInstance.getBody().getDriver().getId(),
                HttpMethod.GET,
                null,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("isAssigned")).isEqualTo(true);
    }
}
