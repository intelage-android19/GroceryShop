package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DebitedTransactionHistoryModel {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("my_customer_auto_id")
    @Expose
    private String myCustomerAutoId;
    @SerializedName("my_contact")
    @Expose
    private String myContact;
    @SerializedName("transfer_contact")
    @Expose
    private String transferContact;
    @SerializedName("transfer_amount")
    @Expose
    private String transferAmount;
    @SerializedName("credited_customer_auto_id")
    @Expose
    private String creditedCustomerAutoId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyCustomerAutoId() {
        return myCustomerAutoId;
    }

    public void setMyCustomerAutoId(String myCustomerAutoId) {
        this.myCustomerAutoId = myCustomerAutoId;
    }

    public String getMyContact() {
        return myContact;
    }

    public void setMyContact(String myContact) {
        this.myContact = myContact;
    }

    public String getTransferContact() {
        return transferContact;
    }

    public void setTransferContact(String transferContact) {
        this.transferContact = transferContact;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getCreditedCustomerAutoId() {
        return creditedCustomerAutoId;
    }

    public void setCreditedCustomerAutoId(String creditedCustomerAutoId) {
        this.creditedCustomerAutoId = creditedCustomerAutoId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
