package com.ptit.edu.store.product.models.view;

public class ClothesStatictisPreview {
    private String id;
    private String name;
    private int price;
    private String logoUrl;
    private int totalOrder;

    public ClothesStatictisPreview() {
    }

    public ClothesStatictisPreview(String id, String name, int price, String logoUrl, Long totalOrder) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.logoUrl = logoUrl;
        this.totalOrder = Math.toIntExact(totalOrder);
    }
    public ClothesStatictisPreview(String id, String name, int price, String logoUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.logoUrl = logoUrl;
        this.totalOrder = 0;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }
}
