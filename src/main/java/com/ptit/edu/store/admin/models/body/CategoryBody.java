package com.ptit.edu.store.admin.models.body;

public class CategoryBody {
    private String name;
    private String logoUrl;
    private int gender;

    public CategoryBody() {
    }

    public CategoryBody(String name, String logoUrl, int gender) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.gender = gender;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
