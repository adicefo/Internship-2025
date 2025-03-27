package com.example.internship_api.data.request;

import org.hibernate.validator.constraints.Range;

public record CompanyPriceInsertRequest(
       @Range(min=1,max=15,message = "Price per kilometer must be between 1 and 15") Double pricePerKilometer
) {

}
