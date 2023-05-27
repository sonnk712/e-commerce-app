package com.LTTBDD.ecommerce_app.model.Location;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class Province {
    @SerializedName("ProvinceID")
    private int provinceId;

    @SerializedName("ProvinceName")
    private String provinceName;

    @SerializedName("CountryID")
    private int countryId;

    @SerializedName("Code")
    private String code;

    @SerializedName("NameExtension")
    private List<String> nameExtension;

    public Province() {
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getNameExtension() {
        return nameExtension;
    }

    public void setNameExtension(List<String> nameExtension) {
        this.nameExtension = nameExtension;
    }
}
