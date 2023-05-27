package com.LTTBDD.ecommerce_app.dto;

import com.google.gson.annotations.SerializedName;

public class DistrictByProvince {

    private int province_id;

    public DistrictByProvince(int province_id) {
        this.province_id = province_id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }
}
