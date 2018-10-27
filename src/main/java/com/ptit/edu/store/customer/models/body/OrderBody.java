package com.ptit.edu.store.customer.models.body;

import java.util.Set;

public class OrderBody {
    private String nameCustomer;
    private String phone;
    private String location;
    private String payments;
    private int totalCost;
    private Set<ItemBody> itemBodySet;

    public OrderBody() {
    }

    public OrderBody(String nameCustomer, String phone, String location, String payments, int totalCost, Set<ItemBody> itemBodySet) {
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.location = location;
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
}
