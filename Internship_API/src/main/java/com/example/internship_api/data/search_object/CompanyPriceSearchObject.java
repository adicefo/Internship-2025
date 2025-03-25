package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompanyPriceSearchObject extends BaseSearchObject{
    private Double pricePerKilometer;
    private LocalDateTime addingDateLTE;
    private LocalDateTime addingDateGTE;
}
