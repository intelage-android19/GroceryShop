package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistoryBaseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allorders")
    @Expose
    private List<Allorder> allorders = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Allorder> getAllorders() {
        return allorders;
    }

    public void setAllorders(List<Allorder> allorders) {
        this.allorders = allorders;
    }

}
