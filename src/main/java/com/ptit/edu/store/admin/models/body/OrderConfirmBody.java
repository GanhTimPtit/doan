package com.ptit.edu.store.admin.models.body;

import java.util.Set;

public class OrderConfirmBody {
    private Set<String> orderIDs;
    private Set<String> customerIDs;
    private int status;

    public OrderConfirmBody() {
    }

    public OrderConfirmBody(Set<String> orderIDs, Set<String> customerIDs, int status) {
        this.orderIDs = orderIDs;
        this.customerIDs = customerIDs;
        this.status = status;
    }

    public Set<String> getOrderIDs() {
        return orderIDs;
    }

    public void setOrderIDs(Set<String> orderIDs) {
        this.orderIDs = orderIDs;
    }

    public Set<String> getCustomerIDs() {
        return customerIDs;
    }

    public void setCustomerIDs(Set<String> customerIDs) {
        this.customerIDs = customerIDs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
