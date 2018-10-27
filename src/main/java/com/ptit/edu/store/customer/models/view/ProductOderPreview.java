package com.ptit.edu.store.customer.models.view;

public class ProductOderPreview {
    private String clothesID;
    private String logoClothes;
    private String nameClothes;
    private String category;
    private String color;
    private String size;
    private int amount;
    private int price;

    public ProductOderPreview() {
    }

    public ProductOderPreview(String clothesID, String logoClothes, String nameClothes, String category, String color, String size, int amount, int price) {
        this.clothesID = clothesID;
        this.logoClothes = logoClothes;
        this.nameClothes = nameClothes;
        this.category = category;
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

    public String getLogoClothes() {
        return logoClothes;
    }

    public void setLogoClothes(String logoClothes) {
        this.logoClothes = logoClothes;
    }

    public String getNameClothes() {
        return nameClothes;
    }

    public void setNameClothes(String nameClothes) {
        this.nameClothes = nameClothes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
