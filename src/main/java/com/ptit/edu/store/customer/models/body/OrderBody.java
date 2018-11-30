package com.ptit.edu.store.customer.models.body;

import java.util.Set;

public class OrderBody {
    private String nameCustomer;
    private String phone;
    private String location;
    private double lat;
    private double log;
    private String payments;
    private int totalCost;
    private Set<ItemBody> itemBodySet;

    public OrderBody() {
    }

    public OrderBody(String nameCustomer, String phone, String location, double lat, double log, String payments, int totalCost, Set<ItemBody> itemBodySet) {
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.location = location;
        this.lat= lat;
        this.log= log;
        this.payments = payments;
        this.totalCost = totalCost;
        this.itemBodySet = itemBodySet;
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

    public Set<ItemBody> getItemBodySet() {
        return itemBodySet;
    }

    public void setItemBodySet(Set<ItemBody> itemBodySet) {
        this.itemBodySet = itemBodySet;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
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
}
