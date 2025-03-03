package com.vthree.rentbaseapplication.ModelClass;

public class ProductOrderModel {
    String productorder_id;
    String product_id;
    String customer_id;
    String product_name;
    String price;
    String address;
    String image;
    String cust_pincode;
    String approve_status,deliver_status,user_id,farmMobile,quantity;

    public ProductOrderModel() {
    }

    public ProductOrderModel(String productorder_id, String product_id, String customer_id, String product_name, String price, String address, String cust_pincode, String image, String user_id, String quantity,String farmMobile, String approve_status, String deliver_status) {
        this.productorder_id = productorder_id;
        this.product_id = product_id;
        this.customer_id = customer_id;
        this.product_name = product_name;
        this.price = price;
        this.address = address;
        this.cust_pincode = cust_pincode;
        this.image = image;
        this.user_id = user_id;
        this.quantity = quantity;
        this.farmMobile = farmMobile;
        this.approve_status = approve_status;
        this.deliver_status = deliver_status;
    }


    public String getProductorder_id() {
        return productorder_id;
    }

    public void setProductorder_id(String productorder_id) {
        this.productorder_id = productorder_id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCust_pincode() {
        return cust_pincode;
    }

    public void setCust_pincode(String cust_pincode) {
        this.cust_pincode = cust_pincode;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public String getDeliver_status() {
        return deliver_status;
    }

    public void setDeliver_status(String deliver_status) {
        this.deliver_status = deliver_status;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFarmMobile() {
        return farmMobile;
    }

    public void setFarmMobile(String farmMobile) {
        this.farmMobile = farmMobile;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
