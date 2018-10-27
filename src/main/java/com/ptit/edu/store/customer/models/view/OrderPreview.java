package com.ptit.edu.store.customer.models.view;

import com.ptit.edu.store.customer.models.data.Item;
import com.ptit.edu.store.customer.models.data.OrderCustomer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OrderPreview {
    private String id;
    private long createdDate;
    private int amount;
    private String nameCustomer;
    private String phone;
    private String location;
    private String payments;
    private int totalCost;
    private Set<ItemPreview> itemPreviews;

    public OrderPreview(OrderCustomer orderCustomer) {
        setId(orderCustomer.getId());
        setTotalCost(orderCustomer.getTotalPrice());
        setNameCustomer(orderCustomer.getNameCustomer());
        setPhone(orderCustomer.getPhone());
        setLocation(orderCustomer.getLocation());
        setPayments(orderCustomer.getPayments());
        setCreatedDate(orderCustomer.getCreatedDate().getTime());
        setAmount(orderCustomer.getItems().size());
        itemPreviews= new HashSet<>();
        for (Item item: orderCustomer.getItems()) {
            itemPreviews.add(new ItemPreview(item));
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ItemPreview> getItemPreviews() {
        return itemPreviews;
    }

    public void setItemPreviews(Set<ItemPreview> itemPreviews) {
        this.itemPreviews = itemPreviews;
    }
}
