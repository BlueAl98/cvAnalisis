package com.blue.cvAnalisis.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;
    private String error;

    public ApiResponse(int status, String message, T data, String error) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    // No-args constructor required by Jackson
    public ApiResponse() {}


    // Convenience constructors
    public ApiResponse(int status, String message, T data) {
        this(status, message, data, null);
    }

    public ApiResponse(int status, String message) {
        this(status, message, null, null);
    }

    // Getters & Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    // Optional: toString() for logging/debugging
    @Override
    public String toString() {
        return "ApiResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}

