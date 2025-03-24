package com.example.internship_api.utils;

public class DistanceUtils {

    //generate distance between two points using Haversine formula
    public static double getDistance(double longitude, double latitude, double otherLongitude, double otherLatitude) {
        double d1 = Math.toRadians(latitude);
        double num1 = Math.toRadians(longitude);
        double d2 = Math.toRadians(otherLatitude);
        double num2 = Math.toRadians(otherLongitude) - num1;
        double d3 = Math.pow(Math.sin((d2 - d1) / 2.0), 2.0) + Math.cos(d1) * Math.cos(d2) * Math.pow(Math.sin(num2 / 2.0), 2.0);

        return 6376500.0 * (2.0 * Math.atan2(Math.sqrt(d3), Math.sqrt(1.0 - d3)));
    }
}
