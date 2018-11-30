package com.ptit.edu.store.product.models.view;

public class ClothesSearchPreview implements java.io.Serializable{
    private String id;
    private String name;


    public ClothesSearchPreview() {
    }

    public ClothesSearchPreview(String id, String name) {
        this.id = id;
        this.name = name;

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

}
