package com.example.internship_api.utils;

import com.example.internship_api.dto.StatisticsInsertRequest;
import com.example.internship_api.entity.Statistics;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping to ignore 'driverId' from being mapped to the entity's 'id' field
        modelMapper.addMappings(new PropertyMap<StatisticsInsertRequest, Statistics>() {
            @Override
            protected void configure() {
                // Skip the id mapping from StatisticsInsertRequest to Statistics entity
                skip(destination.getId());
            }
        });

        // Ignore null values when mapping
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setPropertyCondition(Conditions.isNotNull());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);


        return modelMapper;
    }
}