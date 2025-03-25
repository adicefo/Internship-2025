package com.example.internship_api.data.request;

public record NotificationUpsertRequest(
        String title,
        String content,
        Byte[] image,
        Boolean forClient
) {
}
