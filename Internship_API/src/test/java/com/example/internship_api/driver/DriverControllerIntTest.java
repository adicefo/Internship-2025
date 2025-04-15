package com.example.internship_api.driver;

import com.example.internship_api.dto.DriverDTO;
import com.example.internship_api.dto.GetDriver200Response;
import com.example.internship_api.dto.UserDTO;
import com.example.internship_api.dto.UserInsertRequest;
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

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class DriverControllerIntTest {
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
    void shouldReturnDrivers() {
        ResponseEntity<GetDriver200Response> response = restTemplate.getForEntity("/v2/driver/get", GetDriver200Response.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCount()).isGreaterThanOrEqualTo(1); // could be empty
    }

    @Test
    @Rollback
    void shouldCreateDriverWhenValid() {
        UserInsertRequest request = new UserInsertRequest();
        request.setName("DriverName");
        request.setSurname("DriverSurname");
        request.setUsername("driveruser");
        request.setEmail("driver@example.com");
        request.setPassword("securepass");
        request.setPasswordConfirm("securepass");
        request.setGender(UserInsertRequest.GenderEnum.MALE);
        request.setIsActive(true);

        ResponseEntity<DriverDTO> response = restTemplate.postForEntity("/v2/driver/save", new HttpEntity<>(request), DriverDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUser().getUsername()).isEqualTo("driveruser");
        assertThat(response.getBody().getUser().getId()).isGreaterThan(1);

    }

    @Test
    void shouldNotCreateDriverWhenInvalid() {
        UserInsertRequest request = new UserInsertRequest();
        request.setName("BadDriver");
        request.setSurname("NoSurname");
        request.setUsername("invaliddriver");
        request.setEmail("not-an-email");
        request.setPassword("123");
        request.setPasswordConfirm("123");
        request.setGender(UserInsertRequest.GenderEnum.FEMALE);
        request.setIsActive(true);

        ResponseEntity<Object> response = restTemplate.postForEntity("/v2/driver/save", new HttpEntity<>(request), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldGetDriverById() {
        ResponseEntity<DriverDTO> response = restTemplate.exchange("/v2/driver/getById/1", HttpMethod.GET, null, DriverDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldDeleteDriverById() {
        ResponseEntity<DriverDTO> response = restTemplate.exchange("/v2/driver/delete/1", HttpMethod.DELETE, null, DriverDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Rollback
    void shouldCreateDriverBasedOnUser() {
        // First, create a user
        UserInsertRequest userRequest = new UserInsertRequest();
        userRequest.setName("DriverFromUser");
        userRequest.setSurname("Lastname");
        userRequest.setUsername("driverfromuser");
        userRequest.setEmail("driveruser@example.com");
        userRequest.setPassword("mypassword");
        userRequest.setPasswordConfirm("mypassword");
        userRequest.setTelephoneNumber("061-222-333");
        userRequest.setGender(UserInsertRequest.GenderEnum.MALE);
        userRequest.setIsActive(true);

        ResponseEntity<UserDTO> userResponse = restTemplate.exchange(
                "/v2/users/register",
                HttpMethod.POST,
                new HttpEntity<>(userRequest),
                UserDTO.class
        );

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userResponse.getBody()).isNotNull();

        ResponseEntity<DriverDTO> driverResponse = restTemplate.exchange(
                "/v2/driver/save/" + userResponse.getBody().getId(),
                HttpMethod.POST,
                null,
                DriverDTO.class
        );

        assertThat(driverResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(driverResponse.getBody()).isNotNull();
        assertThat(driverResponse.getBody().getUser().getName()).isEqualTo("DriverFromUser");
    }
}
