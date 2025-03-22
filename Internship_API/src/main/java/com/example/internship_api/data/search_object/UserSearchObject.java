package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class UserSearchObject extends BaseSearchObject {
    private String username;
    private String email;
    private String name;
    private String surname;

}
