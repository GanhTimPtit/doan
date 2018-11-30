package com.ptit.edu.store.customer.models.view;

import java.util.Date;

public class ConfirmOrderPreview {
    private String id;
    private Long createdDate;
    private String nameCustomer;
    private String phone;
    private String locationName;
    private double lat;
    private double log;
    private Integer totalCost;
    private String logoAvatar;
    private int status;
    private String customerID;

    public ConfirmOrderPreview() {
    }

    public ConfirmOrderPreview(String id,
                               Date createdDate,
                               String nameCustomer,
                               String phone,
                               String locationName,
                               double lat,
                               double log,
                               Integer totalCost,
                               String logoAvatar,
                               int status,
                               String customerID) {
        this.id = id;
        this.createdDate = createdDate.getTime();
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.locationName = locationName;
        this.lat= lat;
        this.log= log;
        this.totalCost = totalCost;
        this.logoAvatar= logoAvatar;
        this.status= status;
        this.customerID= customerID;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getLogoAvatar() {
        return logoAvatar;
    }

    public void setLogoAvatar(String logoAvatar) {
        this.logoAvatar = logoAvatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}
