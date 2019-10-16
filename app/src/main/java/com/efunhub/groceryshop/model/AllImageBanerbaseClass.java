package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllImageBanerbaseClass {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allslider")
    @Expose
    private List<ModelImageSlider> allslider = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ModelImageSlider> getAllslider() {
        return allslider;
    }

    public void setAllslider(List<ModelImageSlider> allslider) {
        this.allslider = allslider;
    }

}
