package com.example.internship_api.admin;

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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AdminControllerIntTest {

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
    void shouldReturnAdmins() {
        ResponseEntity<GetAdmin200Response> response = restTemplate.getForEntity("/v2/admin/get", GetAdmin200Response.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCount()).isGreaterThan(0);
        System.out.println("Admins: "+response.getBody().getCount());
    }

    @Test
    @Rollback
    void shouldCreateAdminWhenAdminIsValid() {
        UserInsertRequest newRequest = new UserInsertRequest();
        newRequest.setName("AdminName");
        newRequest.setSurname("AdminSurname");
        newRequest.setUsername("adminuser");
        newRequest.setEmail("admin@example.com");
        newRequest.setPassword("adminpass");
        newRequest.setPasswordConfirm("adminpass");
        newRequest.setGender(UserInsertRequest.GenderEnum.MALE);
        newRequest.setIsActive(true);

        ResponseEntity<AdminDTO> response = restTemplate.postForEntity("/v2/admin/save", new HttpEntity<>(newRequest), AdminDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUser().getUsername()).isEqualTo("adminuser");
        assertThat(response.getBody().getUser().getRegistrationDate()).isBefore(LocalDateTime.now());

    }
    @Test

    void shouldNotCreateNewUserWhenValidationIsInvalid()
    {
        UserInsertRequest newRequest = new UserInsertRequest();
        newRequest.setName("John");
        newRequest.setSurname("Doe");
        newRequest.setUsername("johndoe");
        newRequest.setEmail("john.com");
        newRequest.setPassword("password123");
        newRequest.setPasswordConfirm("password123");
        newRequest.setGender(UserInsertRequest.GenderEnum.MALE);
        newRequest.setIsActive(true);


        ResponseEntity<Object> response = restTemplate.postForEntity("/v2/admin/save", new HttpEntity<>(newRequest), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    void shouldReturnAdminById() {
        ResponseEntity<AdminDTO> response = restTemplate.exchange("/v2/admin/getById/1",HttpMethod.GET,null,AdminDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldUpdateAdminWhenAdminIsValid() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("UpdatedAdmin");
        request.setSurname("UpdatedSurname");
        request.setUsername("updatedadmin");
        request.setEmail("updated@example.com");
        request.setPassword("test");
        request.setPasswordConfirm("test");

        ResponseEntity<AdminDTO> response = restTemplate.exchange(
                "/v2/admin/update/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                AdminDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUser().getUsername()).isEqualTo("updatedadmin");
    }

    @Test
    @Rollback
    void shouldDeleteAdminById() {
        ResponseEntity<AdminDTO> response = restTemplate.exchange(
                "/v2/admin/delete/1",
                HttpMethod.DELETE,
                null,
                AdminDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Rollback
    void shouldCreateAdminBasedOnUser() {
        UserInsertRequest newRequest = new UserInsertRequest();
        newRequest.setName("AdminBasedOnUser");
        newRequest.setSurname("AdminSurname");
        newRequest.setUsername("adminbasedonuser");
        newRequest.setEmail("adminBased@example.com");
        newRequest.setPassword("test");
        newRequest.setPasswordConfirm("test");
        newRequest.setTelephoneNumber("062-773-421");
        newRequest.setGender(UserInsertRequest.GenderEnum.FEMALE);
        newRequest.setIsActive(true);

        ResponseEntity<UserDTO> newUser=restTemplate.exchange("/v2/users/register", HttpMethod.POST, new HttpEntity<>(newRequest), UserDTO.class);

        assertThat(newUser.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newUser.getBody()).isNotNull();

        ResponseEntity<AdminDTO> newAdminBasedOnUser=restTemplate.exchange("/v2/admin/save/"+newUser.getBody().getId().toString(), HttpMethod.POST,null, AdminDTO.class );
        assertThat(newAdminBasedOnUser.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newAdminBasedOnUser.getBody()).isNotNull();
        assertThat(newAdminBasedOnUser.getBody().getUser().getName()).isEqualTo("AdminBasedOnUser");
    }
}


