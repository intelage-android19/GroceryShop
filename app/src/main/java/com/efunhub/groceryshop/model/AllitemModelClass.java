package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllitemModelClass {

    @SerializedName("pmid")
    @Expose
    private String pmid;
    @SerializedName("main_category_ename")
    @Expose
    private String mainCategoryEname;

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

}
