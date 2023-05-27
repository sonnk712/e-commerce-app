package com.LTTBDD.ecommerce_app.model.Shipment;

import com.google.gson.annotations.SerializedName;

public class ShippingServiceRequest {
    @SerializedName("shop_id")
    private int shopId;

    @SerializedName("from_district")
    private int fromDistrict;

    @SerializedName("to_district")
    private int toDistrict;

    public ShippingServiceRequest() {
    }

    public ShippingServiceRequest(int shopId, int fromDistrict, int toDistrict) {
        this.shopId = shopId;
        this.fromDistrict = fromDistrict;
        this.toDistrict = toDistrict;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getFromDistrict() {
        return fromDistrict;
    }

    public void setFromDistrict(int fromDistrict) {
        this.fromDistrict = fromDistrict;
    }

    public int getToDistrict() {
        return toDistrict;
    }

    public void setToDistrict(int toDistrict) {
        this.toDistrict = toDistrict;
    }
}
