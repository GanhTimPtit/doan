package com.ptit.edu.store.product.models.body;

import java.util.Date;

public class ClothesBody {
    private String name;
    private String logoUrl;
    private String categoryID;
    private int cost;
    private String description;

    public ClothesBody() {
    }

    public ClothesBody(String name, String logoUrl, String categoryID, int cost, String description) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.categoryID = categoryID;
        this.cost = cost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
