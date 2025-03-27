package com.example.internship_api.data.request;

import com.example.internship_api.data.RegexConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;


public record UserUpdateRequest(@NonNull @Min(value=2,message = "Name must contain at least 2 charters") @Pattern(regexp = RegexConstants.NAME_SURNAME_REGEX,message = "Start with capital letter")String name,
                                @NonNull @Min(value=2,message = "Surname must contain at least 2 charters") @Pattern(regexp = RegexConstants.NAME_SURNAME_REGEX,message = "Start with capital letter")  String surname,
                                @Pattern(regexp = RegexConstants.TELEPHONE_NUM_REGEX,message="Number format: 06x-xxx-xxx(x)")  String telephoneNumber,
                                @Pattern(regexp = RegexConstants.EMAIL_REGEX,
                                        message = "Email format is:name(name.surname)@something.com")  String email,
                                @Min(value = 5,message = "Username must contain at least 5 charters")  String username,
                                String password,
                                String passwordConfirm) {

}
