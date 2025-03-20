package com.example.internship_api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedResult<T>{
    private List<T> result;
    private int count;
}
