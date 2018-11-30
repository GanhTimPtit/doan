package com.ptit.edu.store.admin.models.body;

public class OrderDeleteBody {
    private String orderID;
    private String customerID;
    private String msg;
    private int status;

    public OrderDeleteBody() {
    }

    public OrderDeleteBody(String orderID, String customerID, String msg, int status) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.msg = msg;
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
