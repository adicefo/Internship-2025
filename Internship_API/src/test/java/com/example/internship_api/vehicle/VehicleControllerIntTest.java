package com.example.internship_api.vehicle;

import com.example.internship_api.dto.GetVehicles200Response;
import com.example.internship_api.dto.VehicleDTO;
import com.example.internship_api.dto.VehicleSearchObject;
import com.example.internship_api.dto.VehicleUpsertRequest;
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
public class VehicleControllerIntTest {

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
    void shouldReturnVehicles() {


        ResponseEntity<GetVehicles200Response> response = restTemplate.getForEntity(
                "/v2/vehicle/get",
                GetVehicles200Response.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetVehicleById() {
        ResponseEntity<VehicleDTO> response = restTemplate.exchange(
                "/v2/vehicle/getById/1",
                HttpMethod.GET,
                null,
                VehicleDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

    @Test
    @Rollback
    void shouldCreateVehicleWhenVehicleIsValid() {
        VehicleUpsertRequest request = new VehicleUpsertRequest();
        request.setName("Test Vehicle");
        request.setAvailable(true);
        request.setAverageFuelConsumption(7.5);
        request.setPrice(55.0);
        request.setImage(null);

        ResponseEntity<VehicleDTO> response = restTemplate.postForEntity(
                "/v2/vehicle/save",
                request,
                VehicleDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Vehicle");
    }

    @Test

    void shouldNotCreateVehicleWhenVehicleIsInvalid() {
        VehicleUpsertRequest request = new VehicleUpsertRequest();
        request.setName("Invalid Vehicle");
        request.setAvailable(true);
        request.setAverageFuelConsumption(5.0);
        request.setPrice(67.0);
        request.setImage(null);

        ResponseEntity<Object> response = restTemplate.postForEntity(
                "/v2/vehicle/save",
                request,
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    void shouldUpdateVehicle() {
        VehicleUpsertRequest request = new VehicleUpsertRequest();
        request.setName("Updated Vehicle");
        request.setAvailable(false);
        request.setAverageFuelConsumption(6.0);
        request.setPrice(45.0);
        request.setImage(null);

        ResponseEntity<VehicleDTO> response = restTemplate.exchange(
                "/v2/vehicle/update/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                VehicleDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Vehicle");
        assertThat(response.getBody().getAvailable()).isFalse();
    }

    @Test
    @Rollback
    void shouldDeleteVehicle() {
        ResponseEntity<VehicleDTO> response = restTemplate.exchange(
                "/v2/vehicle/delete/1",
                HttpMethod.DELETE,
                null,
                VehicleDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
