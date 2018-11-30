package com.ptit.edu.store.product.models.data;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "clothes_recommend")
public class ClothesRecommend {
    public static final String PRIORITY = "priority";
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothesID")
    private Clothes clothes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_clothesID")
    private Clothes clothesRecommend;
    private int priority;

    public ClothesRecommend() {
    }

    public ClothesRecommend(Clothes clothes, Clothes clothesRecommend, int priority) {
        this.clothes = clothes;
        this.clothesRecommend = clothesRecommend;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    public Clothes getClothesRecommend() {
        return clothesRecommend;
    }

    public void setClothesRecommend(Clothes clothesRecommend) {
        this.clothesRecommend = clothesRecommend;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
