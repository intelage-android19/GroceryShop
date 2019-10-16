package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionHistoryBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("credited_history")
    @Expose
    private List<CreditedTransactionHistoryModel> creditedHistory = null;
    @SerializedName("debitted_history")
    @Expose
    private List<DebitedTransactionHistoryModel> debittedHistory = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CreditedTransactionHistoryModel> getCreditedHistory() {
        return creditedHistory;
    }

    public void setCreditedHistory(List<CreditedTransactionHistoryModel> creditedHistory) {
        this.creditedHistory = creditedHistory;
    }

    public List<DebitedTransactionHistoryModel> getDebittedHistory() {
        return debittedHistory;
    }

    public void setDebittedHistory(List<DebitedTransactionHistoryModel> debittedHistory) {
        this.debittedHistory = debittedHistory;
    }

}
