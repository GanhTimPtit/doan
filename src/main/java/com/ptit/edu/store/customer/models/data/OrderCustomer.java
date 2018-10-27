package com.ptit.edu.store.customer.models.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "bill")
public class OrderCustomer {
    public static final String CREATED_DATE = "createdDate";
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private int totalPrice;
    private Date createdDate;
    private String description;
    private String payments;
    private String nameCustomer;
    private String phone;
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID")
    private Customer customer;

    @OneToMany(mappedBy = "orderCustomer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Item> items;


    public OrderCustomer(int totalPrice, String payments, String nameCustomer, String phone, String location, Customer customer) {
        this.totalPrice = totalPrice;
        this.payments = payments;
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.location = location;
        this.customer = customer;
        this.description = "SUCCESS";
    }

    public OrderCustomer() {
    }

    public OrderCustomer(Customer customer, Set<Item> items, int totalPrice) {
        this.totalPrice = totalPrice;
        this.description = "SUCCESS";
        this.customer = customer;
        this.items = items;
    }

    @PrePersist
    public void onPrePersist() {
        this.createdDate = new Date();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date creatDate) {
        this.createdDate = creatDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
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
}
