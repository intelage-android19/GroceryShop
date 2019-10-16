package com.efunhub.groceryshop.model;

public class ModelMechanism {


    private int mechanismid;
    private int mechanismImage;
    private String mechanismTitle;
    private String mechanismDescription;


    public int getMechanismid() {
        return mechanismid;
    }

    public void setMechanismid(int mechanismid) {
        this.mechanismid = mechanismid;
    }

    public int getMechanismImage() {
        return mechanismImage;
    }

    public void setMechanismImage(int mechanismImage) {
        this.mechanismImage = mechanismImage;
    }

    public String getMechanismTitle() {
        return mechanismTitle;
    }

    public void setMechanismTitle(String vegitableName) {
        this.mechanismTitle = vegitableName;
    }

    public String getMechanismDescription() {
        return mechanismDescription;
    }

    public void setMechanismDescription(String mechanismDescription) {
        this.mechanismDescription = mechanismDescription;
    }
}
