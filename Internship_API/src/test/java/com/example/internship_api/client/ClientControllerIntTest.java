package com.example.internship_api.client;

import com.example.internship_api.dto.ClientDTO;
import com.example.internship_api.dto.GetClient200Response;
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
public class ClientControllerIntTest {
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
    void shouldReturnClients() {
        ResponseEntity<GetClient200Response> response = restTemplate.getForEntity("/v2/client/get", GetClient200Response.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCount()).isGreaterThanOrEqualTo(0); // Allow empty for now
    }

    @Test
    @Rollback
    void shouldCreateClientWhenClientIsValid() {
        UserInsertRequest newRequest = new UserInsertRequest();
        newRequest.setName("ClientName");
        newRequest.setSurname("ClientSurname");
        newRequest.setUsername("clientuser");
        newRequest.setEmail("client@example.com");
        newRequest.setPassword("clientpass");
        newRequest.setPasswordConfirm("clientpass");
        newRequest.setGender(UserInsertRequest.GenderEnum.FEMALE);
        newRequest.setIsActive(true);

        ResponseEntity<ClientDTO> response = restTemplate.postForEntity("/v2/client/save", new HttpEntity<>(newRequest), ClientDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUser().getUsername()).isEqualTo("clientuser");
        assertThat(response.getBody().getUser().getName()).isEqualTo("ClientName");
    }

    @Test
    void shouldNotCreateNewClientWhenValidationIsInvalid() {
        UserInsertRequest newRequest = new UserInsertRequest();
        newRequest.setName("Jane");
        newRequest.setSurname("Doe");
        newRequest.setUsername("janedoe");
        newRequest.setEmail("john.doe@edu.fit.ba");
        newRequest.setTelephoneNumber("123456789");
        newRequest.setPassword("12345");
        newRequest.setPasswordConfirm("12345");
        newRequest.setGender(UserInsertRequest.GenderEnum.FEMALE);
        newRequest.setIsActive(true);

        ResponseEntity<Object> response = restTemplate.postForEntity("/v2/client/save", new HttpEntity<>(newRequest), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnClientById() {
        ResponseEntity<ClientDTO> response = restTemplate.exchange("/v2/client/getById/1", HttpMethod.GET, null, ClientDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldDeleteClientById() {
        ResponseEntity<ClientDTO> response = restTemplate.exchange("/v2/client/delete/1", HttpMethod.DELETE, null, ClientDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Rollback
    void shouldCreateClientBasedOnUser() {
        UserInsertRequest newRequest = new UserInsertRequest();
        newRequest.setName("ClientFromUser");
        newRequest.setSurname("ClientFromUserSur");
        newRequest.setUsername("clientfromuser");
        newRequest.setEmail("clientuser@example.com");
        newRequest.setPassword("test");
        newRequest.setPasswordConfirm("test");
        newRequest.setTelephoneNumber("061-123-456");
        newRequest.setGender(UserInsertRequest.GenderEnum.MALE);
        newRequest.setIsActive(true);

        ResponseEntity<UserDTO> userResponse = restTemplate.exchange(
                "/v2/users/register",
                HttpMethod.POST,
                new HttpEntity<>(newRequest),
                UserDTO.class
        );

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userResponse.getBody()).isNotNull();

        ResponseEntity<ClientDTO> clientResponse = restTemplate.exchange(
                "/v2/client/save/" + userResponse.getBody().getId(),
                HttpMethod.POST,
                null,
                ClientDTO.class
        );

        assertThat(clientResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(clientResponse.getBody()).isNotNull();
        assertThat(clientResponse.getBody().getUser().getName()).isEqualTo("ClientFromUser");
    }
}
