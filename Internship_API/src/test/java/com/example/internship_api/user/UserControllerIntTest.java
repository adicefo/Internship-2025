package com.example.internship_api.user;

import com.example.internship_api.dto.GetUsers200Response;
import com.example.internship_api.dto.UserDTO;
import com.example.internship_api.dto.UserInsertRequest;
import com.example.internship_api.dto.UserUpdateRequest;
import com.example.internship_api.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
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
public class UserControllerIntTest {


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
    void shouldReturnUsers() {
        ResponseEntity<String> response = restTemplate.getForEntity("/v2/users/get", String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }
    @Test
    void shouldReturnMoreThan5Users() {
        ResponseEntity<GetUsers200Response> response = restTemplate
                .getForEntity("/v2/users/get", GetUsers200Response.class);


        assertThat(response.getBody().getCount()).isGreaterThan(5);
    }

    @Test
    @Rollback
    void shouldCreateUserWhenUserIsValid() {
        UserInsertRequest newRequest = new UserInsertRequest();
        newRequest.setName("John");
        newRequest.setSurname("Doe");
        newRequest.setUsername("johndoe");
        newRequest.setEmail("johndoe@example.com");
        newRequest.setPassword("password123");
        newRequest.setPasswordConfirm("password123");
        newRequest.setGender(UserInsertRequest.GenderEnum.MALE);
        newRequest.setIsActive(true);


        ResponseEntity<UserDTO> response = restTemplate.postForEntity("/v2/users/register", newRequest, UserDTO.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("johndoe");
        assertThat(response.getBody().getEmail()).isEqualTo("johndoe@example.com");

    }
    @Test

    void shouldNotCreateNewUserWhenValidationIsInvalid()
    {
        UserInsertRequest newRequest = new UserInsertRequest();
        newRequest.setName("john");
        newRequest.setSurname("doe");
        newRequest.setUsername("johndoe");
        newRequest.setEmail("johndoe@example.com");
        newRequest.setPassword("password123");
        newRequest.setPasswordConfirm("password123");
        newRequest.setGender(UserInsertRequest.GenderEnum.MALE);
        newRequest.setIsActive(true);


        ResponseEntity<Object> response = restTemplate.postForEntity("/v2/users/register", new HttpEntity<>(newRequest), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    void shouldUpdateUserWhenUserIsValid()
    {
        ResponseEntity<UserDTO> response = restTemplate.exchange("/v2/users/getById/1", HttpMethod.GET, null, UserDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserDTO existingUser = response.getBody();
        assertThat(existingUser).isNotNull();

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("NewName");
        updateRequest.setSurname("NewSurname");
        updateRequest.setUsername("NewUsername");
        updateRequest.setEmail("newemail@edu.fit.ba");
        updateRequest.setPassword("newpassword");
        updateRequest.setPasswordConfirm("newpassword");

        ResponseEntity<UserDTO> updateResponse = restTemplate.exchange("/v2/users/update/1", HttpMethod.PUT, new HttpEntity<>(updateRequest), UserDTO.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDTO updatedUser = updateResponse.getBody();
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("NewUsername");
        assertThat(updatedUser.getEmail()).isEqualTo("newemail@edu.fit.ba");
        assertThat(updatedUser.getName()).isEqualTo("NewName");

    }

    @Test
    @Rollback
    void shouldDeleteWithAnValidID()
    {
        ResponseEntity<UserDTO> response= restTemplate.exchange("/v2/users/delete/1", HttpMethod.DELETE, null, UserDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}
