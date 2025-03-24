package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleSearchObject extends BaseSearchObject{
    private Boolean available;
    private String name;
}
