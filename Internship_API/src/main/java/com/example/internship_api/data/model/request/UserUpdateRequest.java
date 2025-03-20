package com.example.internship_api.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String surname;
    private String telephoneNumber;
    private String email;
    private String password;
    private String passwordConfirm;
}
