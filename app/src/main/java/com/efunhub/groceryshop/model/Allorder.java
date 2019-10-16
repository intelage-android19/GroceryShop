package com.efunhub.groceryshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Allorder {


    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("dtime")
    @Expose
    private String dtime;
    @SerializedName("tid")
    @Expose
    private String tid;
    @SerializedName("cgst")
    @Expose
    private String cgst;
    @SerializedName("sgst")
    @Expose
    private String sgst;
    @SerializedName("cgstprice")
    @Expose
    private String cgstprice;
    @SerializedName("sgstprice")
    @Expose
    private String sgstprice;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("cutbalance")
    @Expose
    private String cutbalance;
    @SerializedName("paidprice")
    @Expose
    private String paidprice;
    @SerializedName("pmode")
    @Expose
    private String pmode;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_ename")
    @Expose
    private String productEname;
    @SerializedName("product_mname")
    @Expose
    private String productMname;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("product_wqty")
    @Expose
    private String productWqty;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("productimage")
    @Expose
    private String productimage;
    @SerializedName("cstatus")
    @Expose
    private String cstatus;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public String getCgstprice() {
        return cgstprice;
    }

    public void setCgstprice(String cgstprice) {
        this.cgstprice = cgstprice;
    }

    public String getSgstprice() {
        return sgstprice;
    }

    public void setSgstprice(String sgstprice) {
        this.sgstprice = sgstprice;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCutbalance() {
        return cutbalance;
    }

    public void setCutbalance(String cutbalance) {
        this.cutbalance = cutbalance;
    }

    public String getPaidprice() {
        return paidprice;
    }

    public void setPaidprice(String paidprice) {
        this.paidprice = paidprice;
    }

    public String getPmode() {
        return pmode;
    }

    public void setPmode(String pmode) {
        this.pmode = pmode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProductWqty() {
        return productWqty;
    }

    public void setProductWqty(String productWqty) {
        this.productWqty = productWqty;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getCstatus() {
        return cstatus;
    }

    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }


}
