package com.example.internship_api.service;

import com.example.internship_api.data.model.RouteDTO;
import com.example.internship_api.data.model.UserDTO;
import com.example.internship_api.data.request.RouteInsertRequest;
import com.example.internship_api.data.request.RouteUpdateRequest;
import com.example.internship_api.data.request.UserInsertRequest;
import com.example.internship_api.data.request.UserUpdateRequest;
import com.example.internship_api.data.search_object.RouteSearchObject;
import com.example.internship_api.data.search_object.UserSearchObject;

public interface RouteService extends BaseCRUDService<RouteDTO, RouteSearchObject,
        RouteInsertRequest, RouteUpdateRequest> {
    RouteDTO updateFinish(Long id);
    RouteDTO updatePayment(Long id);
}
