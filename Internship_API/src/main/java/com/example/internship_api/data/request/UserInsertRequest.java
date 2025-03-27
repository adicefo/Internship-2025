package com.example.internship_api.data.request;

import com.example.internship_api.data.RegexConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


public record UserInsertRequest (@NonNull @Size(min=2,message = "Name must contain at least 2 charters") @Pattern(regexp = RegexConstants.NAME_SURNAME_REGEX,message = "Name starts with capital letter")String name,
                                 @NonNull @Size(min=2,message = "Surname must contain at least 2 charters") @Pattern(regexp =RegexConstants.NAME_SURNAME_REGEX,message = "Surname starts with capital letter") String surname,
                                 @Size(min = 5,message = "Username must contain at least 5 charters") String username,
                            @Pattern(regexp = RegexConstants.EMAIL_REGEX,
                            message = "Email format is:name(name.surname)@something.com")      String email,
                                  String password,
                                  String passwordConfirm,
                          @Pattern(regexp = RegexConstants.TELEPHONE_NUM_REGEX,message="Number format: 06x-xxx-xxx(x)") String telephoneNumber,
                                 @Pattern(regexp = RegexConstants.GENDER_REGEX, message = "Gender must be 'male', 'female', 'Male', or 'Female'")         String gender,
                                  Boolean isActive){

}
