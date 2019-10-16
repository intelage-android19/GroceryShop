package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileUpdateBaseModel {



    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allprofile")
    @Expose
    private List<ProfileUpdateModel> allprofile = null;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ProfileUpdateModel> getAllprofile() {
        return allprofile;
    }

    public void setAllprofile(List<ProfileUpdateModel> allprofile) {
        this.allprofile = allprofile;
    }

}
