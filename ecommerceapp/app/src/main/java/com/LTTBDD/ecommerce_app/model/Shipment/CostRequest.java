package com.LTTBDD.ecommerce_app.model.Shipment;

import com.google.gson.annotations.SerializedName;

public class CostRequest {
    @SerializedName("shop_id")
    private int shopId;
    @SerializedName("service_id")
    private int serviceId;

    @SerializedName("insurance_value")
    private int insuranceValue;

    @SerializedName("coupon")
    private String coupon;

    @SerializedName("from_district_id")
    private int fromDistrictId;

    @SerializedName("to_district_id")
    private int toDistrictId;

    @SerializedName("to_ward_code")
    private String toWardCode;

    @SerializedName("height")
    private int height;

    @SerializedName("length")
    private int length;

    @SerializedName("weight")
    private int weight;
    @SerializedName("width")
    private int width;


    public CostRequest() {
    }

    public CostRequest(int shopId, int serviceId, int insuranceValue, String coupon, int fromDistrictId, int toDistrictId, String toWardCode, int height, int length, int weight, int width) {
        this.shopId = shopId;
        this.serviceId = serviceId;
        this.insuranceValue = insuranceValue;
        this.coupon = coupon;
        this.fromDistrictId = fromDistrictId;
        this.toDistrictId = toDistrictId;
        this.toWardCode = toWardCode;
        this.height = height;
        this.length = length;
        this.weight = weight;
        this.width = width;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getInsuranceValue() {
        return insuranceValue;
    }

    public void setInsuranceValue(int insuranceValue) {
        this.insuranceValue = insuranceValue;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public int getFromDistrictId() {
        return fromDistrictId;
    }

    public void setFromDistrictId(int fromDistrictId) {
        this.fromDistrictId = fromDistrictId;
    }

    public int getToDistrictId() {
        return toDistrictId;
    }

    public void setToDistrictId(int toDistrictId) {
        this.toDistrictId = toDistrictId;
    }

    public String getToWardCode() {
        return toWardCode;
    }

    public void setToWardCode(String toWardCode) {
        this.toWardCode = toWardCode;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
