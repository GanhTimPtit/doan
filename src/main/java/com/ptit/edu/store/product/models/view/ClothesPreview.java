package com.ptit.edu.store.product.models.view;

import com.ptit.edu.store.product.models.data.Clothes;
import com.ptit.edu.store.product.models.data.Rating;

public class ClothesPreview implements java.io.Serializable{
    private String id;
    private String name;
    private int price;
    private String category;
    private String logoUrl;
    private Integer numberSave;
    private Integer numberAvageOfRate;
    private Float avarageOfRate;

    public ClothesPreview() {
    }

    public ClothesPreview(String id, String name, int price, String category, String logoUrl, Integer numberSave,long countRate, Long sumRate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.logoUrl = logoUrl;
        this.numberSave = numberSave;
        this.numberAvageOfRate =(int) countRate;
        if(this.numberAvageOfRate==0){
            this.avarageOfRate=0.0f;
        }else {
            this.avarageOfRate = (float) sumRate / this.numberAvageOfRate;
        }
    }

    public ClothesPreview(String id, String name, int price, String category, String logoUrl, Integer numberSave, long countRate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.logoUrl = logoUrl;
        this.numberSave = numberSave;
        this.numberAvageOfRate = (int) countRate;
    }

    public Integer getNumberAvageOfRate() {
        return numberAvageOfRate;
    }

    public void setNumberAvageOfRate(Integer numberAvageOfRate) {
        this.numberAvageOfRate = numberAvageOfRate;
    }

    public Float getAvarageOfRate() {
        return avarageOfRate;
    }

    public void setAvarageOfRate(Float avarageOfRate) {
        this.avarageOfRate = avarageOfRate;
    }

    public Float getAvarageOfRate(Clothes clothes) {
        if(clothes.getRatings().size()==0){
            return 0.0f;
        }
        int sum = 0;
        for (Rating rating : clothes.getRatings()) {
            sum += rating.getValue();
        }
        setNumberAvageOfRate(clothes.getRatings().size());
        return (float) sum / clothes.getRatings().size();
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

    public Integer getNumberSave() {
        return numberSave;
    }

    public void setNumberSave(Integer numberSave) {
        this.numberSave = numberSave;
    }
}
