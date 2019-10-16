package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCartDetails {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("wallet")
    @Expose
    private String wallet;
    @SerializedName("cgst")
    @Expose
    private String cgst;
    @SerializedName("sgst")
    @Expose
    private String sgst;
    @SerializedName("cgstprice")
    @Expose
    private Integer cgstprice;
    @SerializedName("sgstprice")
    @Expose
    private Integer sgstprice;
    @SerializedName("cutbalance")
    @Expose
    private Integer cutbalance;
    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("payprice")
    @Expose
    private Integer payprice;
    @SerializedName("paidprice")
    @Expose
    private Integer paidprice;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public Integer getCgstprice() {
        return cgstprice;
    }

    public void setCgstprice(Integer cgstprice) {
        this.cgstprice = cgstprice;
    }

    public Integer getSgstprice() {
        return sgstprice;
    }

    public void setSgstprice(Integer sgstprice) {
        this.sgstprice = sgstprice;
    }

    public Integer getCutbalance() {
        return cutbalance;
    }

    public void setCutbalance(Integer cutbalance) {
        this.cutbalance = cutbalance;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPayprice() {
        return payprice;
    }

    public void setPayprice(Integer payprice) {
        this.payprice = payprice;
    }

    public Integer getPaidprice() {
        return paidprice;
    }

    public void setPaidprice(Integer paidprice) {
        this.paidprice = paidprice;
    }

}
