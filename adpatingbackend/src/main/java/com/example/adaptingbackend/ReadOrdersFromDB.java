package com.example.adaptingbackend;

import java.util.ArrayList;

public class ReadOrdersFromDB {

    private String order_id;
    private int Price;
    private ArrayList<String>  product_id;
    private ArrayList<String>  productQuantity;

    public ReadOrdersFromDB(String order_id,ArrayList<String>  product_id,ArrayList<String>  productQuantity){
        this.order_id=order_id;
        this.product_id=product_id;
        this.productQuantity=productQuantity;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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
