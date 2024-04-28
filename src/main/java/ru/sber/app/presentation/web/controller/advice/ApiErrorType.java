package ru.sber.app.presentation.web.controller.advice;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ApiErrorType {
    @JsonProperty("validation")
    VALIDATION,
    @JsonProperty("business")
    BUSINESS,
    @JsonProperty("system")
    SYSTEM;
    private ApiErrorType(){

    }
}