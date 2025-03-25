package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentSearchObject extends  BaseSearchObject{
    private String status;
    private Integer vehicle_id;
    private Integer client_id;
    private Integer user_id;
    private LocalDateTime rentDate;
    private LocalDateTime endDate;
}
