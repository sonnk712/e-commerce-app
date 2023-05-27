package com.LTTBDD.ecommerce_app.model.Shipment;

import com.google.gson.annotations.SerializedName;

public class ShippingService {
    @SerializedName("service_id")
    private int serviceId;

    @SerializedName("short_name")
    private String shortName;

    @SerializedName("service_type_id")
    private int serviceTypeId;

    public ShippingService(int serviceId, String shortName, int serviceTypeId) {
        this.serviceId = serviceId;
        this.shortName = shortName;
        this.serviceTypeId = serviceTypeId;
    }

    public ShippingService() {
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
