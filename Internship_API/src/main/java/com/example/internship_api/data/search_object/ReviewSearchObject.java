package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearchObject extends BaseSearchObject{
    private String reviewedName;
    private String reviewesName;
}
