package com.ptit.edu.store.product.models.view;

import com.ptit.edu.store.product.models.data.Category;

import java.util.List;

public class ClothesViewModel {
    private String id;
    private String name;
    private int price;
    private String description;
    private long createdDate;
    private String logoUrl;
    private Category category;
    private List<RateClothesViewModel> rateClothesViewModels;
    private int numberSave;
    private boolean isSaved;
    private float avarageOfRate = 0;

    public ClothesViewModel() {
    }

    public ClothesViewModel(String id, String name, int price, String description, String logoUrl, Category category, int numberSave) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.logoUrl = logoUrl;
        this.category = category;
        this.numberSave = numberSave;
        this.isSaved = false;
    }

    public float getAvarageOfRate() {
        return avarageOfRate;
    }

    public float getAvarageOfRate(List<RateClothesViewModel> rateClothesSet) {
        if(rateClothesSet.size()==0){
            return 0;
        }
        int sum = 0;
        for (RateClothesViewModel rateClothes : rateClothesSet) {
            sum += rateClothes.getRating();
        }

        return (float) sum / rateClothesSet.size();
    }

    public void setAvarageOfRate(float avarageOfRate) {
        this.avarageOfRate = avarageOfRate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberSave() {
        return numberSave;
    }

    public void setNumberSave(int numberSave) {
        this.numberSave = numberSave;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean saved) {
        isSaved = saved;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<RateClothesViewModel> getRateClothesViewModels() {
        return rateClothesViewModels;
    }

    public void setRateClothesViewModels(List<RateClothesViewModel> rateClothesViewModels) {
        this.rateClothesViewModels = rateClothesViewModels;
        setAvarageOfRate(getAvarageOfRate(rateClothesViewModels));
    }
}
