package com.nguyenhuutin.model;

import androidx.annotation.NonNull;

public class product {
    private  int ProductID;
    private  String ProductName;
    private  double ProductPrice;

    public product() {
    }

    public product(int productID, String productName, double productPrice) {
        ProductID = productID;
        ProductName = productName;
        ProductPrice = productPrice;
    }

    public int getProductID() {
        return ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    @NonNull
    @Override
    public String toString() {
        return this.ProductID + " - " + this.ProductName + " - " + this.ProductPrice;
    }
}
