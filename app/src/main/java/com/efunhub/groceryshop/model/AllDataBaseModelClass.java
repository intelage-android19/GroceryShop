package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllDataBaseModelClass implements Serializable {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allbestitems")
    @Expose
    private List<ModelBestselling> allbestitems = null;
    @SerializedName("allvegetables")
    @Expose
    private List<ModelVegetables> allvegetables = null;
    @SerializedName("allfruits")
    @Expose
    private List<ModelFruits> allfruits = null;
    @SerializedName("allgrains")
    @Expose
    private List<ModelGrains> allgrains = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ModelBestselling> getAllbestitems() {
        return allbestitems;
    }

    public void setAllbestitems(List<ModelBestselling> allbestitems) {
        this.allbestitems = allbestitems;
    }

    public List<ModelVegetables> getAllvegetables() {
        return allvegetables;
    }

    public void setAllvegetables(List<ModelVegetables> allvegetables) {
        this.allvegetables = allvegetables;
    }

    public List<ModelFruits> getAllfruits() {
        return allfruits;
    }

    public void setAllfruits(List<ModelFruits> allfruits) {
        this.allfruits = allfruits;
    }

    public List<ModelGrains> getAllgrains() {
        return allgrains;
    }

    public void setAllgrains(List<ModelGrains> allgrains) {
        this.allgrains = allgrains;
    }

}
