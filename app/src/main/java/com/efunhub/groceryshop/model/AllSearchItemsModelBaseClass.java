package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllSearchItemsModelBaseClass {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allitems")
    @Expose
    private List<ModelAllSearchItem> allitems = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ModelAllSearchItem> getAllitems() {
        return allitems;
    }

    public void setAllitems(List<ModelAllSearchItem> allitems) {
        this.allitems = allitems;
    }
}
