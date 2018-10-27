package com.ptit.edu.store.customer.models.data;

import com.ptit.edu.store.product.models.data.Clothes;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "bill_product")
public class Item {
    public  static final String CLOTHES = "clothes";
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clothesID")
    private Clothes clothes;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "orderID")
    private OrderCustomer orderCustomer;
    private String color;
    private String size;
    private int amount;
    private int price;

    public Item() {
    }

    public Item(String color, String size, int amount, int price) {
        this.color = color;
        this.size = size;
        this.amount = amount;
        this.price = price;
    }

    public Item(OrderCustomer order, Clothes clothes, String color, String size, int amount, int price) {
        this.orderCustomer = order;
        this.clothes = clothes;
        this.color = color;
        this.size = size;
        this.amount = amount;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    public OrderCustomer getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(OrderCustomer orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
