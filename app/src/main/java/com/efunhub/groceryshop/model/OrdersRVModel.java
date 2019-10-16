package com.efunhub.groceryshop.model;

import java.util.ArrayList;

/**
 * Created by Admin on 19-02-2018.
 */

public class OrdersRVModel {

    private String Oid, Odate;
    private float totalPrice;
    private ArrayList<SpecificOrderRVModel> specificOrderRVModelArrayList;

    public OrdersRVModel() {
    }

    public String getOid() {
        return Oid;
    }

    public void setOid(String oid) {
        Oid = oid;
    }

    public String getOdate() {
        return Odate;
    }

    public void setOdate(String odate) {
        Odate = odate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<SpecificOrderRVModel> getSpecificOrderRVModelArrayList() {
        return specificOrderRVModelArrayList;
    }

    public void setSpecificOrderRVModelArrayList(ArrayList<SpecificOrderRVModel> specificOrderRVModelArrayList) {
        this.specificOrderRVModelArrayList = specificOrderRVModelArrayList;
    }
}
