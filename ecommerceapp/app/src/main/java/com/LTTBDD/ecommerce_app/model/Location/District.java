package com.LTTBDD.ecommerce_app.model.Location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class District {
    @SerializedName("DistrictID")
    private int districtId;

    @SerializedName("ProvinceID")
    private int provinceId;

    @SerializedName("DistrictName")
    private String districtName;

    @SerializedName("Code")
    private String code;

    @SerializedName("NameExtension")
    private List<String> nameExtension;

    public District() {
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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
