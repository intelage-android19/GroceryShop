package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSubCategory {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pmid")
    @Expose
    private String pmid;
    @SerializedName("psid")
    @Expose
    private String psid;
    @SerializedName("sub_category_ename")
    @Expose
    private String subCategoryEname;
    @SerializedName("sub_category_mname")
    @Expose
    private String subCategoryMname;
    @SerializedName("sub_category_image")
    @Expose
    private String subCategoryImage;
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

    public String getPsid() {
        return psid;
    }

    public void setPsid(String psid) {
        this.psid = psid;
    }

    public String getSubCategoryEname() {
        return subCategoryEname;
    }

    public void setSubCategoryEname(String subCategoryEname) {
        this.subCategoryEname = subCategoryEname;
    }

    public String getSubCategoryMname() {
        return subCategoryMname;
    }

    public void setSubCategoryMname(String subCategoryMname) {
        this.subCategoryMname = subCategoryMname;
    }

    public String getSubCategoryImage() {
        return subCategoryImage;
    }

    public void setSubCategoryImage(String subCategoryImage) {
        this.subCategoryImage = subCategoryImage;
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
