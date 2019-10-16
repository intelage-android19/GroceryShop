package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllSubCategoryBaseClass {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allsubcategory")
    @Expose
    private List<ModelSubCategory> allsubcategory = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ModelSubCategory> getAllsubcategory() {
        return allsubcategory;
    }

    public void setAllsubcategory(List<ModelSubCategory> allsubcategory) {
        this.allsubcategory = allsubcategory;
    }

}
