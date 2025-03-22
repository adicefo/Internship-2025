package com.example.internship_api.data.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record UserInsertRequest ( String name,
                                  String surname,
                                  String username,
                                  String email,
                                  String password,
                                  String passwordConfirm,
                                  String telephoneNumber,
                                  String gender,
                                  Boolean isActive){

}
