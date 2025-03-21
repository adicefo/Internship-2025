package com.example.internship_api.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record UserUpdateRequest(String name,
                                String surname,
                                String telephoneNumber,
                                String email,
                                String password,
                                String passwordConfirm) {

}
