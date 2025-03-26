package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSearchObject extends BaseSearchObject {
    private Long driver_id;
    private Long route_id;
    private Boolean accepted;
}
