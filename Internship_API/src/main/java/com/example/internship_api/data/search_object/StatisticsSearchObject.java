package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StatisticsSearchObject extends BaseSearchObject{
    private Long driver_id;
    private LocalDateTime beginningOfWork;
}
