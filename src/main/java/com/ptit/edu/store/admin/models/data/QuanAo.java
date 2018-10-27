package com.ptit.edu.store.admin.models.data;

public class QuanAo {
    private String name;
    private String src;
    private String size;
    private String price;
    private String srcDetail;

    public QuanAo(String name, String src, String size, String price, String srcDetail) {
        this.name = name;
        this.src = src;
        this.size = size;
        this.price = price;
        this.srcDetail = srcDetail;
    }

    public String getSrcDetail() {

        return srcDetail;
    }

    public void setSrcDetail(String srcDetail) {
        this.srcDetail = srcDetail;
    }

    public QuanAo(String name, String src, String size, String price) {
        this.name = name;
        this.src = src;
        this.size = size;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
