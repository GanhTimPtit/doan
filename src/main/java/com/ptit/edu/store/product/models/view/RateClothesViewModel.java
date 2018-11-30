package com.ptit.edu.store.product.models.view;

import com.ptit.edu.store.product.models.data.Rating;

import java.util.Date;

public class RateClothesViewModel {
    private String customerName;
    private String logoUrl;
    private long rateDate;
    private String message;
    private int rating;

    public RateClothesViewModel(String customerFirtsName, String customerLastName, String logoUrl, Date rateDate, String message, int rating) {
        this.customerName = customerFirtsName+" "+customerLastName;
        this.logoUrl = logoUrl;
        this.rateDate = rateDate.getTime();
        this.message = message;
        this.rating = rating;
    }

    public RateClothesViewModel(Rating rating) {
        this.customerName = rating.getCustomer().getFirstName()+" "+ rating.getCustomer().getLastName();
        this.logoUrl = rating.getCustomer().getAvatarUrl();
        this.rateDate = rating.getRateDate().getTime();
        this.message = rating.getMessage();
        this.rating = rating.getValue();
    }

    public RateClothesViewModel() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public long getRateDate() {
        return rateDate;
    }

    public void setRateDate(long rateDate) {
        this.rateDate = rateDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
