package com.ptit.edu.store.customer.models.view;

import com.ptit.edu.store.customer.models.data.Item;
import com.ptit.edu.store.customer.models.data.OrderCustomer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class OrderPreview {
    private String id;
    private Long createdDate;
    private Integer amount;
    private String nameCustomer;
    private String phone;
    private String location;
    private String payments;
    private Integer totalCost;
    private Set<ItemPreview> itemPreviews;

    public OrderPreview() {
    }



    public OrderPreview(String id,
                        Date createdDate,
                        String nameCustomer,
                        String phone,
                        String location,
                        String payments,
                        int totalCost,
                        Set<Item> items) {
        this.id = id;
        this.createdDate = createdDate.getTime();
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.location = location;
        this.payments = payments;
        this.totalCost = totalCost;
        itemPreviews= new HashSet<>();
        for (Item item: items) {
            itemPreviews.add(new ItemPreview(item.getClothes().getId(),item.getClothes().getLogoUrl(), item.getClothes().getName(), item.getColor(), item.getSize(), item.getAmount(),item.getPrice()));
        }
        this.amount = itemPreviews.size();

    }

    public OrderPreview(String id,
                        Date createdDate,
                        String nameCustomer,
                        String phone,
                        String location,
                        String payments,
                        Integer totalCost) {
        this.id = id;
        this.createdDate = createdDate.getTime();
//        this.amount = itemPreviews.size();
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.location = location;
        this.payments = payments;
        this.totalCost = totalCost;
    }

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
            itemPreviews.add(new ItemPreview(item.getClothes().getId(),item.getClothes().getLogoUrl(), item.getClothes().getName(), item.getColor(), item.getSize(), item.getAmount(),item.getPrice()));
        }
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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

    public Set<ItemPreview> getItemPreviews() {
        return itemPreviews;
    }

    public void setItemPreviews(Set<ItemPreview> itemPreviews) {
        this.itemPreviews = itemPreviews;
    }
}
