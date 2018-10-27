package com.ptit.edu.store.product.models.view;

import com.ptit.edu.store.product.models.data.Clothes;
import com.ptit.edu.store.product.models.data.RateClothes;

import java.util.Date;

public class ClothesPreview {
    private String id;
    private String name;
    private int price;
    private String category;
    private String logoUrl;
    private int numberSave;
    private int numberAvageOfRate;
    private float avarageOfRate = 0;
    public ClothesPreview() {
    }

    public ClothesPreview(Clothes clothes) {
        this.id = clothes.getId();
        this.name = clothes.getName();
        this.price = clothes.getPrice();
        this.logoUrl = clothes.getLogoUrl();
        this.numberSave = clothes.getTotalSave();
        this.category= clothes.getCategory().getTitle();
        setAvarageOfRate(getAvarageOfRate(clothes));
    }

    public int getNumberAvageOfRate() {
        return numberAvageOfRate;
    }

    public void setNumberAvageOfRate(int numberAvageOfRate) {
        this.numberAvageOfRate = numberAvageOfRate;
    }

    public float getAvarageOfRate() {
        return avarageOfRate;
    }

    public void setAvarageOfRate(float avarageOfRate) {
        this.avarageOfRate = avarageOfRate;
    }

    public float getAvarageOfRate(Clothes clothes) {
        if(clothes.getRateClothes().size()==0){
            return 0;
        }
        int sum = 0;
        for (RateClothes rateClothes : clothes.getRateClothes()) {
            sum += rateClothes.getRating();
        }
        setNumberAvageOfRate(clothes.getRateClothes().size());
        return (float) sum / clothes.getRateClothes().size();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getNumberSave() {
        return numberSave;
    }

    public void setNumberSave(int numberSave) {
        this.numberSave = numberSave;
    }
}
