package com.example.adaptingbackend;

import java.util.ArrayList;

public class ReadOrdersFromDB {

    private String order_id;
    private int Price;
    private ArrayList<String>  product_id;
    private ArrayList<String>  productQuantity;
    private String code;

    public ReadOrdersFromDB(String order_id,ArrayList<String>  product_id,ArrayList<String>  productQuantity,String code){
        this.order_id=order_id;
        this.product_id=product_id;
        this.productQuantity=productQuantity;
        this.code=code;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public ArrayList<String> getProduct_id() {
        return product_id;
    }

    public void setProduct_id(ArrayList<String> product_id) {
        this.product_id = product_id;
    }

    public ArrayList<String> getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(ArrayList<String> productQuantity) {
        this.productQuantity = productQuantity;
    }
}
