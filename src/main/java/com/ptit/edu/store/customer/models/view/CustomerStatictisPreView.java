package com.ptit.edu.store.customer.models.view;

public class CustomerStatictisPreView {
    private String id;
    private String name;
    private int totalRate;
    private int totalBill;
    private long totalPrice;

    public CustomerStatictisPreView() {
    }

    public CustomerStatictisPreView(String id, String firstName,String lastName, Long totalBill, Long totalPrice) {
        this.id = id;
        this.name = firstName+" "+lastName;

        if(totalBill==null){
            this.totalBill=0;
        }else {
            this.totalBill = Math.toIntExact(totalBill);
        }
        if(totalPrice==null){
            this.totalPrice=0;
        }else {
            this.totalPrice = totalPrice;
        }
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

    public int getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(int totalRate) {
        this.totalRate = totalRate;
    }

    public int getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
