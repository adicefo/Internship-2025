package com.example.internship_api.data.search_object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSearchObject extends  BaseSearchObject{
    private Boolean forClient;
    private String title;
}
