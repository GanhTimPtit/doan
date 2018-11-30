package com.ptit.edu.store.customer.models.view;

import java.util.Date;

public class OrderPreview {
    private String id;
    private Long createdDate;
    private String nameCustomer;
    private String phone;
    private String location;
    private String payments;
    private Integer totalCost;
    private int status;

    public OrderPreview() {
    }


    public OrderPreview(String id,
                        Date createdDate,
                        String nameCustomer,
                        String phone,
                        String location,
                        String payments,
                        int totalCost,
                        int status) {
        this.id = id;
        this.createdDate = createdDate.getTime();
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.location = location;
        this.payments = payments;
        this.totalCost = totalCost;
        this.status= status;
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

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
