package com.example.internship_api.data.model.search_object;

import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseSearchObject {

    private Integer  pageNumber;
    private Integer  pageSize;
}
