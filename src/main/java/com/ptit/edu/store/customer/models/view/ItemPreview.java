package com.ptit.edu.store.customer.models.view;

import com.ptit.edu.store.customer.models.data.Item;

public class ItemPreview {
    private String clothesID;
    private String logoUrl;
    private String name;
    private String color;
    private String size;
    private int amount;
    private int price;

    public ItemPreview() {
    }

    public ItemPreview(String clothesID, String logoUrl, String name, String color, String size, int amount, int price) {
        this.clothesID = clothesID;
        this.logoUrl = logoUrl;
        this.name = name;
        this.color = color;
        this.size = size;
        this.amount = amount;
        this.price = price;
    }

    public String getClothesID() {
        return clothesID;
    }

    public void setClothesID(String clothesID) {
        this.clothesID = clothesID;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
