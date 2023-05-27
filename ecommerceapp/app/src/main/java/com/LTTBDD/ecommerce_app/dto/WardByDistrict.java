package com.LTTBDD.ecommerce_app.dto;

import com.google.gson.annotations.SerializedName;

public class WardByDistrict {
    @SerializedName("district_id")
    private int districtId;

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public WardByDistrict(int districtId) {
        this.districtId = districtId;
    }
}
