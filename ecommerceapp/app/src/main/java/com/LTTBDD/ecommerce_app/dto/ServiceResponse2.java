package com.LTTBDD.ecommerce_app.dto;

import com.google.gson.annotations.SerializedName;

public class ServiceResponse2<T> {
    @SerializedName("code")
    private int code;

    @SerializedName("code_message_value")
    private String codeMessageValue;

    @SerializedName("data")
    private T data;

    @SerializedName("message")
    private String message;

    public ServiceResponse2() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeMessageValue() {
        return codeMessageValue;
    }

    public void setCodeMessageValue(String codeMessageValue) {
        this.codeMessageValue = codeMessageValue;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceResponse2(int code, String codeMessageValue, T data, String message) {
        this.code = code;
        this.codeMessageValue = codeMessageValue;
        this.data = data;
        this.message = message;
    }
}
