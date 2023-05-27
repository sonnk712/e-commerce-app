package com.LTTBDD.ecommerce_app.model.Location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ward {
    @SerializedName("WardCode")
    private String wardCode;

    @SerializedName("DistrictID")
    private int districtId;

    @SerializedName("WardName")
    private String wardName;

    @SerializedName("NameExtension")
    private List<String> nameExtension;

    public Ward() {
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public List<String> getNameExtension() {
        return nameExtension;
    }

    public void setNameExtension(List<String> nameExtension) {
        this.nameExtension = nameExtension;
    }
}
