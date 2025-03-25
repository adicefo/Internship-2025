package com.example.internship_api.data.request;

import org.hibernate.validator.constraints.Range;

public record ReviewUpdateRequest(
        @Range(min=1,max=5) Integer value,
        String description
) {
}
