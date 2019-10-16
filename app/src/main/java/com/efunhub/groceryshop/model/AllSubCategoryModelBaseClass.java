package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllSubCategoryModelBaseClass {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allsubcategoryproducts")
    @Expose
    private List<ModelAllSubCategroy> allsubcategoryproducts = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ModelAllSubCategroy> getAllsubcategoryproducts() {
        return allsubcategoryproducts;
    }

    public void setAllsubcategoryproducts(List<ModelAllSubCategroy> allsubcategoryproducts) {
        this.allsubcategoryproducts = allsubcategoryproducts;
    }

}
