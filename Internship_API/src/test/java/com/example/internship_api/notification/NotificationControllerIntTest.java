package com.example.internship_api.notification;


import com.example.internship_api.dto.GetNotifications200Response;
import com.example.internship_api.dto.NotificationDTO;
import com.example.internship_api.dto.NotificationUpsertRequest;
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
public class NotificationControllerIntTest {
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
    void shouldReturnNotifications() {
        ResponseEntity<GetNotifications200Response> response = restTemplate.exchange(
                "/v2/notification/get",
                HttpMethod.GET,
                null,
                GetNotifications200Response.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetNotificationById() {
        ResponseEntity<NotificationDTO> response = restTemplate.exchange(
                "/v2/notification/getById/1",
                HttpMethod.GET,
                null,
                NotificationDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

    @Test
    @Rollback
    void shouldCreateNotificationWhenNotfIsValid() {
        NotificationUpsertRequest request = new NotificationUpsertRequest();
        request.setTitle("New Notification");
        request.setContent("This is a test notification.");
        request.setImage(null);
        request.setForClient(true);

        ResponseEntity<NotificationDTO> response = restTemplate.postForEntity(
                "/v2/notification/save",
                request,
                NotificationDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("New Notification");
        assertThat(response.getBody().getForClient()).isTrue();
    }


    @Test
    @Rollback
    void shouldUpdateNotificationWhenNotfIsValid() {
        NotificationUpsertRequest request = new NotificationUpsertRequest();
        request.setTitle("Updated Notification");
        request.setContent("Updated content.");
        request.setImage(null);
        request.setForClient(false);

        ResponseEntity<NotificationDTO> response = restTemplate.exchange(
                "/v2/notification/update/1",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                NotificationDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Notification");
        assertThat(response.getBody().getForClient()).isFalse();
    }

    @Test
    @Rollback
    void shouldDeleteNotification() {
        ResponseEntity<NotificationDTO> response = restTemplate.exchange(
                "/v2/notification/delete/1",
                HttpMethod.DELETE,
                null,
                NotificationDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
