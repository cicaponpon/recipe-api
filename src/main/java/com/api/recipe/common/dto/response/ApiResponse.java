package com.api.recipe.common.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T result;
    private List<String> errors;

    public ApiResponse(boolean success, String message, T result, List<String> errors) {
        super();
        this.success = success;
        this.message = message;
        this.result = result;
        this.errors = errors;
    }

    public ApiResponse(boolean success, String message, T result) {
        super();
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public ApiResponse(boolean success, String message) {
        super();
        this.success = success;
        this.message = message;
    }
}

