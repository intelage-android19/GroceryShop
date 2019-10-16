package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllOffersBaseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("alloffers")
    @Expose
    private List<ModelAllOffers> alloffers = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ModelAllOffers> getAlloffers() {
        return alloffers;
    }

    public void setAlloffers(List<ModelAllOffers> alloffers) {
        this.alloffers = alloffers;
    }
}
