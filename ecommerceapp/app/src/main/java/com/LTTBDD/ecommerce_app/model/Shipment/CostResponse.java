package com.LTTBDD.ecommerce_app.model.Shipment;

public class CostResponse {
    private double total;

    public CostResponse(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
