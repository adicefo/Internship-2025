package com.example.internship_api.service;

import com.example.internship_api.data.model.RouteDTO;
import com.example.internship_api.data.model.VehicleDTO;
import com.example.internship_api.data.request.RouteInsertRequest;
import com.example.internship_api.data.request.RouteUpdateRequest;
import com.example.internship_api.data.request.VehicleUpsertRequest;
import com.example.internship_api.data.search_object.RouteSearchObject;
import com.example.internship_api.data.search_object.VehicleSearchObject;

public interface VehicleService extends BaseCRUDService<VehicleDTO, VehicleSearchObject,
        VehicleUpsertRequest, VehicleUpsertRequest>{

}
