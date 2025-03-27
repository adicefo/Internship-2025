package com.example.internship_api.data;

public class RegexConstants {
    public static final String NAME_SURNAME_REGEX="[A-Z][a-z]{2,}";
    public static final String EMAIL_REGEX="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}(\\,[a-zA-Z]{2,6})?$";
    public static final String TELEPHONE_NUM_REGEX="^06\\d-\\d{3}-\\d{3,4}$";
    public static final String GENDER_REGEX="^(male|female|Male|Female)$";

}
