package com.LTTBDD.ecommerce_app.model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private int id;
    private double totalPrice;
    private String deliveryAddress;
    private String note;
    private int status;
    private Date createdDate;
    private int userId;

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Order(int id, double totalPrice, String deliveryAddress, String note, int status, Date createdDate, int userId) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.note = note;
        this.status = status;
        this.createdDate = createdDate;
        this.userId = userId;
    }

    public Order() {
    }
}
