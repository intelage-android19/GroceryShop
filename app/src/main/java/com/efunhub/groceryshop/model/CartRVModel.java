package com.efunhub.groceryshop.model;

import java.io.Serializable;

public class CartRVModel implements Serializable {

    public  String prodName, prodPrice,productID,img,subTotal;
    public int prodQty;
    String productEName,productMName;
    String pWeight;
    String wQty;

    public CartRVModel() {}

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getProdQty() {
        return prodQty;
    }

    public void setProdQty(int prodQty) {
        this.prodQty = prodQty;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getProductEName() {
        return productEName;
    }

    public void setProductEName(String productEName) {
        this.productEName = productEName;
    }

    public String getProductMName() {
        return productMName;
    }

    public void setProductMName(String productMName) {
        this.productMName = productMName;
    }

    public String getpWeight() {
        return pWeight;
    }

    public void setpWeight(String pWeight) {
        this.pWeight = pWeight;
    }

    public String getwQty() {
        return wQty;
    }

    public void setwQty(String wQty) {
        this.wQty = wQty;
    }
}
