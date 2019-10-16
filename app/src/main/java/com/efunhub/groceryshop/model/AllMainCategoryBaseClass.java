package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllMainCategoryBaseClass {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allmaincategory")
    @Expose
    private List<ModelMainCategory> allmaincategory = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ModelMainCategory> getAllmaincategory() {
        return allmaincategory;
    }

    public void setAllmaincategory(List<ModelMainCategory> allmaincategory) {
        this.allmaincategory = allmaincategory;
    }

}
