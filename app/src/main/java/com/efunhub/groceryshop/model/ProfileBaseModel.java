package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileBaseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allprofile")
    @Expose
    private List<Profilemodel> allprofile = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Profilemodel> getAllprofile() {
        return allprofile;
    }

    public void setAllprofile(List<Profilemodel> allprofile) {
        this.allprofile = allprofile;
    }


}
