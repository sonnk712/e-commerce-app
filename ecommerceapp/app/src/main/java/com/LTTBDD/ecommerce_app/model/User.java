package com.LTTBDD.ecommerce_app.model;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String password;
    private String gmail;
    private String name;
    private String address;
    private int gender;
    private Date birthday;
    private String img;
    private String phoneNumber;
    private int cartId;
    private int provinceId;
    private int districtId;
    private String wardId;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getGmail() {
        return gmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public User(int id, String username, String password, String gmail, String name, String address, int gender, Date birthday, String img, String phoneNumber, int cartId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gmail = gmail;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.birthday = birthday;
        this.img = img;
        this.phoneNumber = phoneNumber;
        this.cartId = cartId;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return gmail;
    }

    public void setEmail(String gmail) {
        this.gmail = gmail;
    }
}
