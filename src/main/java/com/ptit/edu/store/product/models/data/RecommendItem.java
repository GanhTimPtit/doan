package com.ptit.edu.store.product.models.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "recommend_item")
public class RecommendItem {
    @Id
    private String id;
    private String idItem1;
    private String idItem2;
    private String idItem3;
    private String idItem4;

    public RecommendItem() {
    }

    public RecommendItem(String id, String idItem1, String idItem2, String idItem3, String idItem4) {
        this.id = id;
        this.idItem1 = idItem1;
        this.idItem2 = idItem2;
        this.idItem3 = idItem3;
        this.idItem4 = idItem4;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdItem1() {
        return idItem1;
    }

    public void setIdItem1(String idItem1) {
        this.idItem1 = idItem1;
    }

    public String getIdItem2() {
        return idItem2;
    }

    public void setIdItem2(String idItem2) {
        this.idItem2 = idItem2;
    }

    public String getIdItem3() {
        return idItem3;
    }

    public void setIdItem3(String idItem3) {
        this.idItem3 = idItem3;
    }

    public String getIdItem4() {
        return idItem4;
    }

    public void setIdItem4(String idItem4) {
        this.idItem4 = idItem4;
    }
}
