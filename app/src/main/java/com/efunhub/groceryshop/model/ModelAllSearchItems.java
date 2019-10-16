package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelAllSearchItems {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("pmid")
    @Expose
    private String pmid;
    @SerializedName("psid")
    @Expose
    private String psid;
    @SerializedName("product_ename")
    @Expose
    private String productEname;
    @SerializedName("product_mname")
    @Expose
    private String productMname;
    @SerializedName("product_img")
    @Expose
    private String productImg;
    @SerializedName("product_edetails")
    @Expose
    private String productEdetails;
    @SerializedName("product_mdetails")
    @Expose
    private String productMdetails;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("final_price")
    @Expose
    private String finalPrice;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getProductEname() {
        return productEname;
    }

    public void setProductEname(String productEname) {
        this.productEname = productEname;
    }

    public String getProductMname() {
        return productMname;
    }

    public void setProductMname(String productMname) {
        this.productMname = productMname;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductEdetails() {
        return productEdetails;
    }

    public void setProductEdetails(String productEdetails) {
        this.productEdetails = productEdetails;
    }

    public String getProductMdetails() {
        return productMdetails;
    }

    public void setProductMdetails(String productMdetails) {
        this.productMdetails = productMdetails;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
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
