package com.example.adaptingbackend;

public class Order {
    private String order_id;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String product_id;
    private String productQuantity;


    public Order() {

    }

    public Order(String productName, String quantity, String price, String product_id) {
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        this.product_id = product_id;

    }

    public Order(String order_id, String product_id, String productQuantity) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.productQuantity = productQuantity;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
