package com.example.myapplication;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.example.myapplication.MainShop.mCartItemCount;

public class Order {

    Map<Product,Integer> m_order;
    double m_value =0;

    Order(){
        m_order= new LinkedHashMap<>();
    }

    void addToOrder(Product product){
        if(m_order.containsKey(product))
            m_order.put(product,m_order.get(product) + 1);
        else
            m_order.put(product,1);

        m_value += product.get_value();
    }

    void removeFromCart(Product product) {
        mCartItemCount= mCartItemCount-m_order.get(product).intValue();
        m_value -= product.get_value()*m_order.get(product).intValue();
        m_order.remove(product);

    }

    void removeOneFromCart(Product product){
        if(m_order.get(product)!=1) {
            m_order.put(product, m_order.get(product) - 1);
        }
        else {
            m_order.remove(product);
        }
        m_value -= product.get_value();
    }

    int getQuantity(Product product)
    {
        return m_order.get(product);
    }

    Set getProducts()
    {
        return m_order.keySet();
    }


    void empty(){
        m_order.clear();
        m_value=0;
    }

    double getValue()
    {
        return m_value;
    }

    int getSize()
    {
        return m_order.size();
    }
}



