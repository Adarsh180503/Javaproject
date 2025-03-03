package com.vthree.rentbaseapplication.ModelClass;

import java.io.Serializable;

public class ProductModel{
    String product_id;
    String product_name;
    String quantity;
    String user_id,farmer_mobile;
    String description;
    String image;
    String city,price;
    String pincode;//approve_status,deliver_status;

    public ProductModel() {
    }

    public ProductModel(String product_id, String product_name, String quantity, String user_id, String farmer_mobile,String description, String image, String price,String city,String pincode) {//,String approve_status,String deliver_status
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.user_id = user_id;
        this.farmer_mobile=farmer_mobile;
        this.description = description;
        this.image = image;
        this.price=price;
        this.city = city;
        this.pincode=pincode;
//        this.approve_status = approve_status;
//        this.deliver_status=deliver_status;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFarmer_mobile() {
        return farmer_mobile;
    }

    public void setFarmer_mobile(String farmer_mobile) {
        this.farmer_mobile = farmer_mobile;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


//    public String getApprove_status() {
//        return approve_status;
//    }
//
//    public void setApprove_status(String approve_status) {
//        this.approve_status = approve_status;
//    }
//
//    public String getDeliver_status() {
//        return deliver_status;
//    }
//
//    public void setDeliver_status(String deliver_status) {
//        this.deliver_status = deliver_status;
//    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
