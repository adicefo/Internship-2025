package com.example.internship_api.data.model.search_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchObject extends BaseSearchObject {
    private String username;
    private String email;
    private String name;
    private String surname;

}
