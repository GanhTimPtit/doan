package com.ptit.edu.store.customer.models.view;

public class HeaderProfile {
    private String customerID;
    private String fullName;
    private String avatarUrl;
    private String email;
    private String phone;
    public HeaderProfile(String firstName, String lastName,String customerID, String avatarUrl,String email, String phone) {
        this.fullName = firstName+" "+lastName;
        this.customerID = customerID;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.phone= phone;
    }

    public HeaderProfile() {

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerID() {

        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
