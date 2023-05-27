package com.LTTBDD.ecommerce_app.common.config.dto;

import com.google.gson.annotations.SerializedName;

public class ServiceResponse <T>{
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;


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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ServiceResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ServiceResponse() {
    }
}
