package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteSearchObject extends BaseSearchObject{
    private String status;
    private Integer client_id;
    private Integer driver_id;
    private Integer user_id;
}
