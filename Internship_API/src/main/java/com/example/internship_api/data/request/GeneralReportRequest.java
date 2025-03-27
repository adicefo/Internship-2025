package com.example.internship_api.data.request;

import org.hibernate.validator.constraints.Range;

public record GeneralReportRequest(
     @Range(min=1,max=12,message = "Please send valid month")   int month,
     @Range(min=2024,message = "Please send valid year after 2024")   int year

) {
}
