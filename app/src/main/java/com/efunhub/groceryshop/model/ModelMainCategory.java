package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelMainCategory {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pmid")
    @Expose
    private String pmid;
    @SerializedName("main_category_ename")
    @Expose
    private String mainCategoryEname;
    @SerializedName("main_category_mname")
    @Expose
    private String mainCategoryMname;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getMainCategoryEname() {
        return mainCategoryEname;
    }

    public void setMainCategoryEname(String mainCategoryEname) {
        this.mainCategoryEname = mainCategoryEname;
    }

    public String getMainCategoryMname() {
        return mainCategoryMname;
    }

    public void setMainCategoryMname(String mainCategoryMname) {
        this.mainCategoryMname = mainCategoryMname;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
