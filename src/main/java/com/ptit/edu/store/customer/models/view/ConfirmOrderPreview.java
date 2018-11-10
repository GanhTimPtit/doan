package com.ptit.edu.store.customer.models.view;

import java.util.Date;

public class ConfirmOrderPreview {
    private String id;
    private Long createdDate;
    private String nameCustomer;
    private String phone;
    private String location;
    private Integer totalCost;
    private String logoAvatar;

    public ConfirmOrderPreview() {
    }

    public ConfirmOrderPreview(String id,
                               Date createdDate,
                               String nameCustomer,
                               String phone,
                               String location,
                               Integer totalCost,
                               String logoAvatar) {
        this.id = id;
        this.createdDate = createdDate.getTime();
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.location = location;
        this.totalCost = totalCost;
        this.logoAvatar= logoAvatar;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
